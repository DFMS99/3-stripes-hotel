package actors

import akka.actor.{Actor, ActorRef, Props}

class RoomChatActor(out: ActorRef, manager: ActorRef, username: String) extends Actor {
    manager ! RoomChatManager.NewRoomChatter(username, self)

    def receive = {
        case roomMsg: String => manager ! RoomChatManager.RoomMessage(username, roomMsg)
        case RoomChatActor.SendMessageToClient(userFrom, msg) => out ! s"$userFrom: $msg"
        case m => println("Unhandled message in ChatActor: " + m)
    }
}

object RoomChatActor {
    def props(out: ActorRef, manager: ActorRef, username: String) = Props(new RoomChatActor(out, manager, username))

    case class SendMessageToClient(userFrom: String, msg: String)
}
