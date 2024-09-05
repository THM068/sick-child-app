package sick.child.server

import io.getquill.Literal
import io.getquill.jdbczio.Quill
import sick.child.repository.city.{CityRepository, CityRepositoryLive}
import sick.child.routes.{AssetRoutes, CityRoutes, NotFoundRoute}
import zio.http.{Middleware, Path, Routes, Server}
import zio.{ZIO, ZLayer}

case class AppServer (notFoundRoute: NotFoundRoute, assetRoute: AssetRoutes, cityView: CityRoutes) {
  val dataSourceLive  = Quill.DataSource.fromPrefix("quill")
  val postgresLive    = Quill.Postgres.fromNamingStrategy(Literal)
  val apps =   cityView.apps ++ assetRoute.apps ++ notFoundRoute.apps
  val port = 9998

  def runServer(): ZIO[CityRepositoryLive with CityRepository, Throwable, Unit] = for {
    _ <- ZIO.debug(s"Starting server on http://localhost:${port}")
    _ <- Server.serve(apps)
      .provide(Server.defaultWithPort(port),
               CityRepositoryLive.live,
               CityRepository.live,
               postgresLive,
               dataSourceLive)
  } yield ()

}

object AppServer {
  val layer: ZLayer[AssetRoutes with NotFoundRoute with CityRoutes, Nothing, AppServer] = ZLayer.fromFunction(AppServer.apply _)
}
