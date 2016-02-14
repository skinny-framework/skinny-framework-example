package controller

import org.scalatest._
import skinny._
import skinny.test._
import org.joda.time._
import model._

// NOTICE before/after filters won't be executed by default
class CompaniesControllerSpec extends FunSpec with Matchers with DBSettings {

  def createMockController = new CompaniesController with MockController
  def newCompany = FactoryGirl(Company).create()

  describe("CompaniesController") {

    describe("shows companies") {
      it("shows HTML response") {
        val controller = createMockController
        controller.showResources
        controller.status should equal(200)
        controller.renderCall.map(_.path) should equal(Some("/companies/index"))
        controller.contentType should equal("text/html; charset=utf-8")
      }
    }

    describe("shows a company") {
      it("shows HTML response") {
        val company = newCompany
        val controller = createMockController
        controller.showResource(company.id)
        controller.status should equal(200)
        controller.getFromRequestScope[Company]("item") should equal(Some(company))
        controller.renderCall.map(_.path) should equal(Some("/companies/show"))
      }
    }

    describe("shows new resource input form") {
      it("shows HTML response") {
        val controller = createMockController
        controller.newResource
        controller.status should equal(200)
        controller.renderCall.map(_.path) should equal(Some("/companies/new"))
      }
    }

    describe("creates a company") {
      it("succeeds with valid parameters") {
        val controller = createMockController
        controller.prepareParams(
          "name" -> "dummy",
          "url" -> "dummy"
        )
        controller.createResource
        controller.status should equal(200)
      }

      it("fails with invalid parameters") {
        val controller = createMockController
        controller.prepareParams() // no parameters
        controller.createResource
        controller.status should equal(400)
        controller.errorMessages.size should be > (0)
      }
    }

    it("shows a resource edit input form") {
      val company = newCompany
      val controller = createMockController
      controller.editResource(company.id)
      controller.status should equal(200)
      controller.renderCall.map(_.path) should equal(Some("/companies/edit"))
    }

    it("updates a company") {
      val company = newCompany
      val controller = createMockController
      controller.prepareParams(
        "name" -> "dummy",
        "url" -> "dummy"
      )
      controller.updateResource(company.id)
      controller.status should equal(200)
    }

    it("destroys a company") {
      val company = newCompany
      val controller = createMockController
      controller.destroyResource(company.id)
      controller.status should equal(200)
    }

  }

}
