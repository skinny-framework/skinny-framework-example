package model

import skinny.DBSettings
import skinny.test._
import org.scalatest.fixture.FlatSpec
import scalikejdbc._, SQLInterpolation._
import scalikejdbc.scalatest._
import org.joda.time._

class MemberSpec extends FlatSpec with DBSettings with AutoRollback {
}
