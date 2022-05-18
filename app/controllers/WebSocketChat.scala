package controllers

import javax.inject._

import scala.collection.mutable
import play.api._
import play.api.mvc._
import play.api.libs.json._
import akka.actor.Actor
import play.api.libs.streams.ActorFlow
import akka.actor.ActorSystem
import akka.stream.Materializer
import actors.ChatActor
import akka.actor.Props
import actors.ChatManager 

@Singleton
class WebSocketChat @Inject()(val controllerComponents: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
extends BaseController {
    val manager = system.actorOf(Props[ChatManager], "Manager")

    def index = Action {implicit request =>
        Ok(views.html.chatpage())
    }

    def socket = WebSocket.accept[String, String] {request =>
        ActorFlow.actorRef {out =>
            ChatActor.props(out, manager)
        }

    }
}