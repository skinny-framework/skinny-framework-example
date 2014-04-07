package integrationtest

import org.scalatra.test.scalatest._
import skinny._
import skinny.test._
import org.joda.time._
import _root_.controller.Controllers
import model._

class CompaniesController_IntegrationTestSpec extends ScalatraFlatSpec with SkinnyTestSupport with DBSettings {
  addFilter(Controllers.companies, "/*")

  def newCompany = FactoryGirl(Company).create()

  it should "show companies" in {
    get("/companies") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "show a company in detail" in {
    get(s"/companies/${newCompany.id}") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "show new entry form" in {
    get(s"/companies/new") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "create a company" in {
    post(s"/companies",
      "name" -> "dummy",
      "url" -> "dummy") {
        logBodyUnless(403)
        status should equal(403)
      }

    withSession("csrf-token" -> "valid_token") {
      post(s"/companies",
        "name" -> "dummy",
        "url" -> "dummy",
        "csrf-token" -> "valid_token") {
          logBodyUnless(302)
          status should equal(302)
          val id = header("Location").split("/").last.toLong
          Company.findById(id).isDefined should equal(true)
        }
    }
  }

  it should "show the edit form" in {
    get(s"/companies/${newCompany.id}/edit") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "update a company" in {
    post(s"/companies/${newCompany.id}",
      "name" -> "dummy",
      "url" -> "dummy") {
        logBodyUnless(403)
        status should equal(403)
      }

    withSession("csrf-token" -> "valid_token") {
      post(s"/companies/${newCompany.id}",
        "name" -> "dummy",
        "url" -> "dummy",
        "csrf-token" -> "valid_token") {
          logBodyUnless(302)
          status should equal(302)
        }
    }
  }

  it should "delete a company" in {
    delete(s"/companies/${newCompany.id}") {
      logBodyUnless(403)
      status should equal(403)
    }
    withSession("csrf-token" -> "valid_token") {
      delete(s"/companies/${newCompany.id}?csrf-token=valid_token") {
        logBodyUnless(200)
        status should equal(200)
      }
    }
  }

}
