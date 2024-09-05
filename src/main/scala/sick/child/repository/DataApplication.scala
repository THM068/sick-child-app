package sick.child.repository

import io.getquill.Literal
import io.getquill.jdbczio.Quill

object DataApplication {
  val dataSourceLive  = Quill.DataSource.fromPrefix("quill")
  val postgresLive    = Quill.Postgres.fromNamingStrategy(Literal)
}
