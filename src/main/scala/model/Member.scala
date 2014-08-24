package model

import skinny.orm._, feature._
import scalikejdbc._
import org.joda.time._

case class Member(
  id: Long,
  name: String,
  nickname: String,
  birthday: Option[LocalDate] = None,
  companyId: Option[Long] = None,
  company: Option[Company] = None,
  createdAt: DateTime,
  updatedAt: DateTime)

object Member extends SkinnyCRUDMapper[Member] with TimestampsFeature[Member] {
  override lazy val tableName = "members"
  override lazy val defaultAlias = createAlias("m")
  override def extract(rs: WrappedResultSet, rn: ResultName[Member]): Member =
    autoConstruct(rs, rn, "company")

  belongsTo[Company](
    Company, (member, company) => member.copy(company = company))
    .byDefault
}
