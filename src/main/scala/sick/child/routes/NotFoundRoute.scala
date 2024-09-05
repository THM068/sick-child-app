package sick.child.routes
import html.notFound
import sick.child.server.renderer.ViewRenderer
import sick.child.server.renderer.ViewRenderer.render
import zio.ZLayer
import zio.http.{HttpApp, Method, Response, RoutePattern, Routes, Status, handler}

class NotFoundRoute {
  val notFoundRoute = RoutePattern.any -> handler {
    val content = notFound()
    render(content.body,Response.notFound.status)
  }

  val index = Method.GET / "" -> handler {
    val content = html.IndexPage(List("Apple", "Oranges", "Mangoes"), "Home")
    ViewRenderer.render(content.body)
  }


  val apps: HttpApp[Any] = Routes(index,notFoundRoute)
    .handleError { t: Throwable => Response.text("The error is " + t).status(Status
      .InternalServerError) }
    .toHttpApp

  def handle(throwable: Throwable) = {
    Response.text("The error is " + throwable).status(Status.InternalServerError)
  }
}

object NotFoundRoute {
  val layer: ZLayer[Any, Nothing, NotFoundRoute] = ZLayer.succeed(apply)

  def apply = new NotFoundRoute()

}
