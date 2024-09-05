package sick.child
import io.getquill._
import io.getquill.jdbczio.Quill
//import sick.child.repository.DataApplication.{dataSourceLive, postgresLive}
import sick.child.routes.NotFoundRoute
import sick.child.server.AppServer
import zio.Console.ConsoleLive.printLine
import zio._

import java.sql.SQLException


object Main extends ZIOAppDefault {

  case class DataService(quill: Quill.Postgres[Literal]) {

    import quill._

    val cities = quote(query[City])

    def citiesByName = quote((name: String) => cities.filter(city => city.name == name))
  }

  case class ApplicationLive(dataService: DataService) {
    import dataService.quill
    import dataService.quill._

    def getCityByName(name: String): ZIO[Any, SQLException, List[City]] =
      quill.run(dataService.citiesByName(lift(name)))

    def getAllCities(): ZIO[Any, SQLException, List[City]] = quill.run(dataService.cities)
  }

  object Application {
    def getCityByName(name: String) = ZIO.serviceWithZIO[ApplicationLive](_.getCityByName(name))

    def getAllCities() = ZIO.serviceWithZIO[ApplicationLive](_.getAllCities())
  }

    val dataServiceLive = ZLayer.fromFunction(DataService.apply _)
    val applicationLive = ZLayer.fromFunction(ApplicationLive.apply _)
    val dataSourceLive  = Quill.DataSource.fromPrefix("quill")
    val postgresLive    = Quill.Postgres.fromNamingStrategy(Literal)

  override def run =
    (for {
      city      <- Application.getCityByName("Zaanstad")
      _         <- printLine(city)
      allCities <- Application.getAllCities()
      _         <- printLine(allCities.take(10))
      _ <- ZIO.serviceWithZIO[AppServer](_.runServer())
    } yield ())
      .provide(
        AppServer.layer,
        NotFoundRoute.layer,
        applicationLive,
        dataServiceLive,
        dataSourceLive,
        postgresLive,
        ZLayer.Debug.mermaid)
}
