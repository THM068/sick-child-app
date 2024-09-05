package sick.child.repository.city

import zio.ZIO

object CityService {
  def getCityByName(name: String) = ZIO.serviceWithZIO[CityRepositoryLive](_.getCityByName(name))

  def getAllCities() = ZIO.serviceWithZIO[CityRepositoryLive](_.getAllCities())
}
