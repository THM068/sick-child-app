package sick.child.repository.city

import io.getquill.Literal
import io.getquill.jdbczio.Quill
import sick.child.City
import zio.ZLayer


case class CityRepository(quill: Quill.Postgres[Literal]) {

  import quill._

  val cities = quote(query[City])

  def citiesByName = quote((name: String) => cities.filter(city => city.name == name))
}

object CityRepository {
  val live: ZLayer[Quill.Postgres[Literal], Nothing, CityRepository] =
    ZLayer.fromFunction(CityRepository.apply _)
}
