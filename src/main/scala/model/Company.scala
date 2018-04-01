package model

import skinny.orm._, feature._
import scalikejdbc._
import org.joda.time._

case class Company(
  id: Long,
  name: String,
  url: String,
  createdAt: DateTime,
  updatedAt: DateTime)

object Company extends SkinnyCRUDMapper[Company] with TimestampsFeature[Company] {
  override lazy val tableName = "companies"
  override lazy val defaultAlias = createAlias("c")
  override def extract(rs: WrappedResultSet, rn: ResultName[Company]): Company = autoConstruct(rs, rn)
}
