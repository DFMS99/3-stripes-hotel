package actors

import akka.actor.{Actor, ActorRef}

class WhisperManager extends Actor {

    private var whisperChatters = scala.collection.mutable.Map[String, ActorRef]()

    def receive = {
        case WhisperManager.NewWhisper(username, whisperActor) => {
            whisperChatters(username) = whisperActor
            println(s"New whisper registered $username full list $whisperChatters")
        }
        case WhisperManager.WhisperMessage(userFrom, userTo, msg) => {
            println(s"full list of whispersChatters $whisperChatters")
            whisperChatters.get(userTo) match {
                case Some(whisperActor) =>{
                    println(s"WhisperManager: User found $userTo")
                    whisperActor ! WhisperActor.ReceiveWhisper(userFrom, msg)
                }
                case None => {
                    println(s"WhisperManager: User not found $userTo, ")
                    sender() ! WhisperActor.WhisperChatterNotFound(userTo)
                }
            }
        }
        case m => println("Unhandled message in WhisperManager: " + m)
    }
}

object WhisperManager {
    case class NewWhisper(username: String, whisperActor: ActorRef)
    case class WhisperMessage(userFrom: String, userTo: String ,msg: String)
}
