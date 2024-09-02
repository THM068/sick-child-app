package sick.child
import io.getquill._
import io.getquill.jdbczio.Quill
import zio.Console.ConsoleLive.printLine
import zio._

import java.sql.SQLException


object Main extends ZIOAppDefault {

  case class DataService(quill: Quill.Postgres[Literal]) {

    import quill._

    val accounts = quote(query[Account])

    def accountsByEmail = quote((email: String) => accounts.filter(acc => acc.email == email))
  }

  case class ApplicationLive(dataService: DataService) {
    import dataService.quill
    import dataService.quill._

    def getAccountsByEmail(email: String): ZIO[Any, SQLException, List[Account]] =
      quill.run(dataService.accountsByEmail(lift(email)))

    def getAllAccounts(): ZIO[Any, SQLException, List[Account]] = quill.run(dataService.accounts)
  }

  object Application {
    def getAccountsByEmail(email: String) = ZIO.serviceWithZIO[ApplicationLive](_.getAccountsByEmail(email))

    def getAllAccounts() = ZIO.serviceWithZIO[ApplicationLive](_.getAllAccounts())
  }

    val dataServiceLive = ZLayer.fromFunction(DataService.apply _)
    val applicationLive = ZLayer.fromFunction(ApplicationLive.apply _)
    val dataSourceLive  = Quill.DataSource.fromPrefix("quill")
    val postgresLive    = Quill.Postgres.fromNamingStrategy(Literal)




  override def run =
    (for {
      emails      <- Application.getAccountsByEmail("thando.mafela@gmail.com")
      _         <- printLine(emails)
      allAccounts <- Application.getAllAccounts()
      _         <- printLine(allAccounts)
    } yield ()).provide(applicationLive, dataServiceLive, dataSourceLive, postgresLive)
}
