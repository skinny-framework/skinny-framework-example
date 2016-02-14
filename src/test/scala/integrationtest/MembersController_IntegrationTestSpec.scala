package integrationtest

import skinny._
import skinny.test._
import org.joda.time._
import _root_.controller.Controllers
import model._

class MembersController_IntegrationTestSpec extends SkinnyFlatSpec with SkinnyTestSupport with DBSettings {
  addFilter(Controllers.members, "/*")

  def newMember = FactoryGirl(Member).create()

  it should "show members" in {
    get("/members") {
      logBodyUnless(200)
      status should equal(200)
    }
    get("/members/") {
      logBodyUnless(200)
      status should equal(200)
    }
    get("/members.json") {
      logBodyUnless(200)
      status should equal(200)
    }
    get("/members.xml") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "show a member in detail" in {
    get(s"/members/${newMember.id}") {
      logBodyUnless(200)
      status should equal(200)
    }
    get(s"/members/${newMember.id}.xml") {
      logBodyUnless(200)
      status should equal(200)
    }
    get(s"/members/${newMember.id}.json") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "show new entry form" in {
    get(s"/members/new") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "create a member" in {
    post(
      s"/members",
      "name" -> "dummy",
      "nickname" -> "dummy",
      "birthday" -> skinny.util.DateTimeUtil.toString(new LocalDate())
    ) {
        logBodyUnless(403)
        status should equal(403)
      }

    withSession("csrf-token" -> "valid_token") {
      post(
        s"/members",
        "name" -> "dummy",
        "nickname" -> "dummy",
        "birthday" -> skinny.util.DateTimeUtil.toString(new LocalDate()),
        "csrf-token" -> "valid_token"
      ) {
          logBodyUnless(302)
          status should equal(302)
          val id = header("Location").split("/").last.toLong
          Member.findById(id).isDefined should equal(true)
        }
    }
  }

  it should "show the edit form" in {
    get(s"/members/${newMember.id}/edit") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "update a member" in {
    put(
      s"/members/${newMember.id}",
      "name" -> "dummy",
      "nickname" -> "dummy",
      "birthday" -> skinny.util.DateTimeUtil.toString(new LocalDate())
    ) {
        logBodyUnless(403)
        status should equal(403)
      }

    withSession("csrf-token" -> "valid_token") {
      put(
        s"/members/${newMember.id}",
        "name" -> "dummy",
        "nickname" -> "dummy",
        "birthday" -> skinny.util.DateTimeUtil.toString(new LocalDate()),
        "csrf-token" -> "valid_token"
      ) {
          logBodyUnless(302)
          status should equal(302)
        }
    }
  }

  it should "delete a member" in {
    delete(s"/members/${newMember.id}") {
      logBodyUnless(403)
      status should equal(403)
    }
    withSession("csrf-token" -> "valid_token") {
      delete(s"/members/${newMember.id}?csrf-token=valid_token") {
        logBodyUnless(200)
        status should equal(200)
      }
    }
  }

}
