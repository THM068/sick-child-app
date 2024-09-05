package sick.child.server

import sick.child.routes.{AssetRoutes, NotFoundRoute}
import zio.http.{Middleware, Path, Routes, Server}
import zio.{ZIO, ZLayer}

case class AppServer (notFoundRoute: NotFoundRoute, assetRoute: AssetRoutes) {

  val apps =   assetRoute.apps ++ notFoundRoute.apps
  val port = 9998

  def runServer(): ZIO[Any, Throwable, Unit] = for {
    _ <- ZIO.debug(s"Starting server on http://localhost:${port}")
    _ <- Server.serve(apps)
      .provide(Server.defaultWithPort(port))
  } yield ()

}

object AppServer {
  val layer: ZLayer[AssetRoutes with NotFoundRoute, Nothing, AppServer] = ZLayer.fromFunction(AppServer.apply _)
}
