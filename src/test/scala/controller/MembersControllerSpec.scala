package controller

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import skinny._
import skinny.test._
import org.joda.time._
import model._

// NOTICE before/after filters won't be executed by default
class MembersControllerSpec extends FunSpec with ShouldMatchers with DBSettings {

  def createMockController = new MembersController with MockController
  def newMember = FactoryGirl(Member).create()

  describe("MembersController") {

    describe("shows members") {
      it("shows HTML response") {
        val controller = createMockController
        controller.showResources()
        controller.status should equal(200)
        controller.renderCall.map(_.path) should equal(Some("/members/index"))
        controller.contentType should equal("text/html; charset=utf-8")
      }

      it("shows JSON response") {
        implicit val format = Format.JSON
        val controller = createMockController
        controller.showResources()
        controller.status should equal(200)
        controller.renderCall.map(_.path) should equal(Some("/members/index"))
        controller.contentType should equal("application/json; charset=utf-8")
      }
    }

    describe("shows a member") {
      it("shows HTML response") {
        val member = newMember
        val controller = createMockController
        controller.showResource(member.id)
        controller.status should equal(200)
        controller.requestScope[Member]("item") should equal(Some(member))
        controller.renderCall.map(_.path) should equal(Some("/members/show"))
      }
    }

    describe("shows new resource input form") {
      it("shows HTML response") {
        val controller = createMockController
        controller.newResource()
        controller.status should equal(200)
        controller.renderCall.map(_.path) should equal(Some("/members/new"))
      }
    }

    describe("creates a member") {
      it("succeeds with valid parameters") {
        val controller = createMockController
        controller.prepareParams(
          "name" -> "dummy",
          "nickname" -> "dummy",
          "birthday" -> skinny.util.DateTimeUtil.toString(new LocalDate()))
        controller.createResource()
        controller.status should equal(200)
      }

      it("fails with invalid parameters") {
        val controller = createMockController
        controller.prepareParams() // no parameters
        controller.createResource()
        controller.status should equal(400)
        controller.errorMessages.size should be > (0)
      }
    }

    it("shows a resource edit input form") {
      val member = newMember
      val controller = createMockController
      controller.editResource(member.id)
      controller.status should equal(200)
      controller.renderCall.map(_.path) should equal(Some("/members/edit"))
    }

    it("updates a member") {
      val member = newMember
      val controller = createMockController
      controller.prepareParams(
        "name" -> "dummy",
        "nickname" -> "dummy",
        "birthday" -> skinny.util.DateTimeUtil.toString(new LocalDate()))
      controller.updateResource(member.id)
      controller.status should equal(200)
    }

    it("destroys a member") {
      val member = newMember
      val controller = createMockController
      controller.destroyResource(member.id)
      controller.status should equal(200)
    }

  }

}
