package integrationtest

import skinny.test.SkinnyTestSupport
import skinny.test.SkinnyFlatSpec
import _root_.controller.Controllers

class RootController_IntegrationTestSpec extends SkinnyFlatSpec with SkinnyTestSupport {
  addFilter(Controllers.root, "/*")

  it should "show top page" in {
    get("/") {
      status should equal(200)
    }
  }

}
