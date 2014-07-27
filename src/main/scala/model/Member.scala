package model

import skinny.orm._, feature._
import scalikejdbc._
import org.joda.time._

// If your model has +23 fields, switch this to normal class and mixin scalikejdbc.EntityEquality.
case class Member(
  id: Long,
  name: String,
  nickname: String,
  birthday: Option[LocalDate] = None,
  createdAt: DateTime,
  updatedAt: DateTime)

object Member extends SkinnyCRUDMapper[Member] with TimestampsFeature[Member] {
  override lazy val tableName = "members"
  override lazy val defaultAlias = createAlias("m")

  override def extract(rs: WrappedResultSet, rn: ResultName[Member]): Member = new Member(
    id = rs.get(rn.id),
    name = rs.get(rn.name),
    nickname = rs.get(rn.nickname),
    birthday = rs.get(rn.birthday),
    createdAt = rs.get(rn.createdAt),
    updatedAt = rs.get(rn.updatedAt)
  )
}
