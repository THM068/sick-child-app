package sick.child.routes
import html.CityView
import sick.child.repository.city.{CityRepositoryLive, CityService}
import sick.child.server.renderer.ViewRenderer
import zio.ZLayer
import zio.http.{HttpApp, Method, Response, Routes, Status, handler}

class CityRoutes {

  val allCities = Method.GET / "city" -> handler {
    for {
      cities <- CityService.getAllCities()
    } yield {
      val content = html.CityView(cities = cities)
      ViewRenderer.render(content.body)
    }
  }

  val apps: HttpApp[CityRepositoryLive] = Routes(allCities)
    .handleError { t: Throwable => Response.text("The error is " + t).status(Status
      .InternalServerError) }
    .toHttpApp
}

object CityRoutes {
  val live = ZLayer.succeed(new CityRoutes())
}
