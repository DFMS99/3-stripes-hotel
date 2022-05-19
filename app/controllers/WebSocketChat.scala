package controllers

import actors.WhisperActor.SendWhisper
import actors.{RoomChatActor, RoomChatManager, WhisperActor, WhisperManager}
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._

import javax.inject._

@Singleton
class WebSocketChat @Inject()(val controllerComponents: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
extends BaseController {

    implicit val sendWhisperJson = Json.format[SendWhisper]
    implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[SendWhisper, String]

    val roomChatManager = system.actorOf(Props[RoomChatManager], "RoomManager")
    val whisperManager = system.actorOf(Props[WhisperManager])

    def index = Action {implicit request =>
        Ok(views.html.chatpage())
    }

    def roomChatSocket(username: String) = WebSocket.accept[String, String] {request =>
        ActorFlow.actorRef { out =>
            RoomChatActor.props(out, roomChatManager, username)
        }
    }

    def whisperSocket(username: String) = WebSocket.accept[SendWhisper, String] {request =>
        ActorFlow.actorRef { out =>
            WhisperActor.props(out, whisperManager, username)
        }
    }
}