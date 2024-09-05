package sick.child.repository.city

import sick.child.City
import zio.{ZIO, ZLayer}

import java.sql.SQLException

case class CityRepositoryLive(cityRepository: CityRepository) {
  import cityRepository.quill
  import cityRepository.quill._

  def getCityByName(name: String): ZIO[Any, SQLException, List[City]] =
    quill.run(cityRepository.citiesByName(lift(name)))

  def getAllCities(): ZIO[Any, SQLException, List[City]] = quill.run(cityRepository.cities.take(20))
}

object CityRepositoryLive {
  val live: ZLayer[CityRepository, Nothing, CityRepositoryLive] =
    ZLayer.fromFunction(CityRepositoryLive.apply _)
}
