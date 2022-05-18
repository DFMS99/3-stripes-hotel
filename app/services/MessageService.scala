package services

import models._
import scala.collection.mutable
import scala.util.Try
import scala.util.Failure
import java.util.UUID
import scala.util.Success

class MessageService {
    private val messagesDB = mutable.ListBuffer[Message]()
    private val wisperDB = mutable.ListBuffer[Wisper]()
}
