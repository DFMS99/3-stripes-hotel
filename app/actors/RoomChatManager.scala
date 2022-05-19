package actors

import actors.RoomChatManager.{NewRoomChatter, RoomMessage}
import akka.actor.Actor
import akka.actor.ActorRef

private case class Chatter(user: String, chatActor: ActorRef)

class RoomChatManager extends Actor {

    private var roomChatters = List.empty[Chatter]

    def receive = {
        case NewRoomChatter(username, chatActor) => {
            val chatter = Chatter(username, chatActor)
            roomChatters ::= chatter
            println("New chatter join the room "+chatter)
        }
        case RoomMessage(userFrom, msg) =>
            roomChatters.foreach( _.chatActor ! RoomChatActor.SendMessageToClient(userFrom,msg))
        case m => println("Unhandled message in ChatManager: " + m)
    }
}

object RoomChatManager {
    case class NewRoomChatter(username: String, roomChatActor: ActorRef)
    case class RoomMessage(userFrom: String, msg: String)
}
