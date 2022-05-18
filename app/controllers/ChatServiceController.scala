package controllers

import javax.inject._

import scala.collection.mutable
import play.api._
import play.api.mvc._
import play.api.libs.json._
import java.util.UUID
import models.ChatSeviceModel

case class Users(username: String,id: UUID)
case class NewUser(username: String)


case class Rooms(id: UUID, roomname: String)
case class NewRoom(roomname: String)


case class Messages(roomId: UUID, fromUser: String, msg: String)
case class Wispers(toUser: String, fromUser: String,  msg: String)
case class NewMessages(msg: String, fromUser: String)
case class NewWisper(msg: String, fromUser: String)

@Singleton
class ChatServiceController @Inject()(val controllerComponents: ControllerComponents)
extends BaseController {
    private val chatService = new mutable.ListBuffer[Users]()
    private val roomsList = new mutable.ListBuffer[Rooms]()
    private val msgList = new mutable.ListBuffer[Messages]()
    private val wisperList = new mutable.ListBuffer[Wispers]()

    implicit val chatServiceJson = Json.format[Users]
    implicit val newChatServiceJson = Json.format[NewUser]

    implicit val roomJson = Json.format[Rooms]
    implicit val newroomJson = Json.format[NewRoom]
    roomsList += Rooms(UUID.randomUUID(), "General")

    implicit val msgJson = Json.format[Messages]
    implicit val NewMsgJson = Json.format[NewMessages]
    implicit val wisJson = Json.format[Wispers]
    implicit val NewWisJson = Json.format[NewWisper]

   

    def getAll(): Action[AnyContent] = Action {
        if (chatService.isEmpty) {
            NoContent
        } else {
            Ok(Json.toJson(chatService))
        }
    }

    def getAllRoom(): Action[AnyContent] = Action {
        if (roomsList.isEmpty) {
            NoContent
        } else {
            Ok(Json.toJson(roomsList))
        }
    }

    def getById(itemId: Long) = Action {
        val foundItem = chatService.find(_.id == itemId)
        foundItem match {
            case Some(item) => Ok(Json.toJson(item))
            case None => NotFound
        }
    }

    def getByIdRoom(itemId: Long) = Action {
        val foundItem = roomsList.find(_.id == itemId)
        foundItem match {
            case Some(item) => Ok(Json.toJson(item))
            case None => NotFound
        }
    }


    def addNewUser() = Action { implicit request => 
        val content = request.body 
        val jsonObject = content.asJson 
        val user: Option[NewUser] = 
            jsonObject.flatMap( 
                Json.fromJson[NewUser](_).asOpt 
            )
        user match {
            case Some(newItem) =>
                if(chatService.filter(_.username == newItem.username).isEmpty){
                    if (chatService.isEmpty) {
                        val randId = UUID.randomUUID()
                        val toBeAdded = Users(newItem.username, randId)
                        chatService += toBeAdded
                        Created(Json.toJson(toBeAdded))
                    } else {
                        val randId = UUID.randomUUID()
                        val toBeAdded = Users(newItem.username, randId)
                        chatService += toBeAdded
                        Created(Json.toJson(toBeAdded))
                    }
                } else {
                    Ok("This username is already used")
                }
            case None =>
                BadRequest
        }
    }


    def addNewRoom() = Action { implicit request => 
        val content = request.body 
        val jsonObject = content.asJson 
        val rooms: Option[NewRoom] = 
            jsonObject.flatMap( 
                Json.fromJson[NewRoom](_).asOpt 
            )
        rooms match {
            case Some(newItem) =>
                if(roomsList.filter(_.roomname == newItem.roomname).isEmpty){
                    if (roomsList.isEmpty) {
                        val randId = UUID.randomUUID()
                        val toBeAdded = Rooms(randId, newItem.roomname)
                        roomsList += toBeAdded
                        Created(Json.toJson(toBeAdded))
                    } else {
                        val randId = UUID.randomUUID()
                        val toBeAdded = Rooms(randId, newItem.roomname)
                        roomsList += toBeAdded
                        Created(Json.toJson(toBeAdded))
                    }
                } else {
                    Ok("This name is already used")
                }
            case None =>
                BadRequest
        }
    }

    def deleteUser(itemUser: String): Action[AnyContent] = Action {
        val foundItem = chatService.find(_.username == itemUser)
        foundItem match {
            case Some(item) =>
                chatService -= item 
                Ok("This user has been deleted")
                Ok(Json.toJson(item))
            case None => NotFound
        }
    }

    def deleteRoom(itemRoom: String): Action[AnyContent] = Action {
        val foundItem = roomsList.find(_.roomname == itemRoom)
        foundItem match {
            case Some(item) =>
                roomsList -= item 
                Ok("This user has been deleted")
                Ok(Json.toJson(item))
            case None => NotFound
        }
    }


    def addMessages(roomId: String) = Action { implicit request => 
        val content = request.body 
        val jsonObject = content.asJson 
        val msg: Option[NewMessages] = 
            jsonObject.flatMap( 
                Json.fromJson[NewMessages](_).asOpt 
            )
        msg match {
            case Some(newMsg) =>
                val foundRoom = roomsList.find(_.id == UUID.fromString(roomId))
                foundRoom match {
                    case Some(room) => 
                        val foundUser = chatService.find(_.username == newMsg.fromUser)
                        foundUser match {
                            case Some(user) => 
                                val msgAdded = Messages(UUID.fromString(roomId), newMsg.fromUser, newMsg.msg)
                                msgList += msgAdded
                                Created(Json.toJson(msgAdded))
                            case None => NotFound
                        }
                    case None => NotFound
                }
            case None =>
                BadRequest
        }
    }

    def addWisper(username: String) = Action { implicit request => 
        val content = request.body 
        val jsonObject = content.asJson 
        val wisper: Option[NewWisper] = 
            jsonObject.flatMap( 
                Json.fromJson[NewWisper](_).asOpt 
            )
        wisper match {
            case Some(newWis) =>
                val foundUser = chatService.find(_.username == username)
                foundUser match {
                    case Some(user) =>
                        val foundUser = chatService.find(_.username == newWis.fromUser)
                        foundUser match {
                            case Some(userWis) => 
                                val wisAdded = Wispers(username, newWis.fromUser, newWis.msg)
                                wisperList += wisAdded
                                Created(Json.toJson(wisAdded))
                            case None => NotFound
                        }
                    case None => NotFound
                }
            case None =>
                BadRequest
        }
    }

    def getWisper(username: String, fromUser: String) = Action {
        val foundUser = wisperList.find(_.toUser == username)
        foundUser match {
            case Some(userItem) => 
                val foundWispers = wisperList.filter(_.toUser == userItem.toUser)
                val showWispers = foundWispers.filter(_.fromUser == fromUser)
                Ok(Json.toJson(showWispers))
            case None => NotFound
        }

    }



}
