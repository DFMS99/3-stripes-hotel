package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.libs.json._
import play.api.test.Helpers._
import scala.collection.mutable


class ChatServiceControllerSpec extends PlaySpec {

    "ChatServiceController" must {
        val controller = new ChatServiceController(stubControllerComponents())  
        "give back users" in {
            val result = controller.getAll().apply(FakeRequest())
            val bodyText = contentAsJson(result)
            bodyText mustBe(Json.toJson(controller.chatService))
        }
    }
  
}
