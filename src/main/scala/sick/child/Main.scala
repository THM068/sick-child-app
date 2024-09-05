package sick.child
import io.getquill._
import io.getquill.jdbczio.Quill
import sick.child.repository.city.{CityRepository, CityRepositoryLive}
import sick.child.routes.{AssetRoutes, CityRoutes}
import sick.child.routes.NotFoundRoute
import sick.child.server.AppServer
import zio._


object Main extends ZIOAppDefault {

    val dataSourceLive  = Quill.DataSource.fromPrefix("quill")
    val postgresLive    = Quill.Postgres.fromNamingStrategy(Literal)

  override def run =
    for {
      _ <- ZIO.serviceWithZIO[AppServer](_.runServer())
        .provide(
          AppServer.layer,
          //Routes
          NotFoundRoute.layer,
          AssetRoutes.layer,
          CityRoutes.live,
          //Repositories
          CityRepositoryLive.live,
          CityRepository.live,
          dataSourceLive,
          postgresLive,

          ZLayer.Debug.mermaid)
    } yield ()

}
