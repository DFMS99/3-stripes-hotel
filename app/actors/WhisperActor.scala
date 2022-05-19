package actors

import actors.WhisperActor.{ReceiveWhisper, SendWhisper, WhisperChatterNotFound}
import akka.actor.{Actor, ActorRef, Props}

class WhisperActor(out: ActorRef, whisperManager: ActorRef, username: String) extends Actor {
    println(s"WhisperActor: $username connected and ready for whispers")
    whisperManager ! WhisperManager.NewWhisper(username, self)

    out ! s"WhisperActor: $username connected and ready for whispers"

    def receive = {
        case SendWhisper(to, msg) => whisperManager ! WhisperManager.WhisperMessage(username, to, msg)
        case ReceiveWhisper(from, msg) => out ! s"$from: $msg"
        case WhisperChatterNotFound(to) => out ! s"The user $to is not connected"
        case m => println("Unhandled message in WhisperActor: " + m)
    }
}

object WhisperActor {

    def props(out: ActorRef, whisperManager: ActorRef,  username: String) = Props(new WhisperActor(out, whisperManager, username))

    case class SendWhisper(to: String, msg: String)
    case class ReceiveWhisper(from: String, msg: String )
    case class WhisperChatterNotFound(to: String)
}
