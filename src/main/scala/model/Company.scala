package model

import skinny.orm._, feature._
import scalikejdbc._
import org.joda.time._

// If your model has +23 fields, switch this to normal class and mixin scalikejdbc.EntityEquality.
case class Company(
  id: Long,
  name: String,
  url: String,
  createdAt: DateTime,
  updatedAt: DateTime)

object Company extends SkinnyCRUDMapper[Company] with TimestampsFeature[Company] {
  override lazy val tableName = "companies"
  override lazy val defaultAlias = createAlias("c")

  override def extract(rs: WrappedResultSet, rn: ResultName[Company]): Company = new Company(
    id = rs.get(rn.id),
    name = rs.get(rn.name),
    url = rs.get(rn.url),
    createdAt = rs.get(rn.createdAt),
    updatedAt = rs.get(rn.updatedAt)
  )
}
