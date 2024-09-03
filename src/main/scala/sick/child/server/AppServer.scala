package sick.child.server

import sick.child.routes.NotFoundRoute
import zio.http.{Middleware, Path, Routes, Server}
import zio.{ZIO, ZLayer}

case class AppServer (notFoundRoute: NotFoundRoute) {
  val serveResourcesApp = Routes.empty.toHttpApp @@  Middleware.serveResources(Path.empty /
    "static")
  val apps =  notFoundRoute.apps ++ serveResourcesApp
  val port = 9998

  def runServer(): ZIO[Any, Throwable, Unit] = for {
    _ <- ZIO.debug(s"Starting server on http://localhost:${port}")
    _ <- Server.serve(apps)
      .provide(Server.defaultWithPort(port))
  } yield ()

}

object AppServer {
  val layer: ZLayer[NotFoundRoute, Nothing, AppServer] = ZLayer.fromFunction(AppServer.apply _)
}
