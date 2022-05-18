package controllers

import javax.inject._

import scala.collection.mutable
import play.api._
import play.api.mvc._
import play.api.libs.json._
import java.util.UUID
import models._
import services.UserService
import services.RoomService
import scala.util.Success
import scala.util.Failure

@Singleton
class ChatServiceController @Inject()(val controllerComponents: ControllerComponents)
extends BaseController {

    val userService: UserService = new UserService()
    val roomService: RoomService = new RoomService()

    implicit val chatServiceJson = Json.format[User]
    implicit val newChatServiceJson = Json.format[NewUser]

    implicit val roomJson = Json.format[Room]
    implicit val newroomJson = Json.format[NewRoom]

    implicit val msgJson = Json.format[Message]
    implicit val NewMsgJson = Json.format[NewMessage]
    implicit val wisJson = Json.format[Wisper]
    implicit val NewWisJson = Json.format[NewWisper]

    def getAllUsers(): Action[AnyContent] = Action {

        val result = userService.getAll()

        if (result.isEmpty) {
            NoContent
        } else {
            Ok(Json.toJson(result))
        }
    }

    def getAllRooms(): Action[AnyContent] = Action {
        val result = roomService.getAll()
        
        if (result.isEmpty) {
            NoContent
        } else {
            Ok(Json.toJson(result))
        }
    }

    def getUserByUsername(username: String) = Action {

        val userOpt = userService.findByUsername()

        userOpt match {
            case Some(user) => Ok(Json.toJson(user))
            case None => NotFound
        }
    }

    def getRoomById(id: String) = Action {

        val roomOpt = roomService.findById()

        roomOpt match {
            case Some(room) => Ok(Json.toJson(room))
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
                userService.addUser(newItem.username) match {
                    case Success(user) => Ok(Json.toJson(user))
                    case Failure(error) => BadRequest(error.getMessage())
                }
            case None =>
                BadRequest
        }
    }

    def addNewRoom() = Action { implicit request => 
        val content = request.body 
        val jsonObject = content.asJson 
        val room: Option[NewRoom] = 
            jsonObject.flatMap( 
                Json.fromJson[NewRoom](_).asOpt 
            )
        room match {
            case Some(newItem) => 
                roomService.addRoom(newItem.roomname) match {
                    case Success(room) => Ok
                    case Failure(error) => BadRequest(error.getMessage())
                }
            case None =>
                BadRequest
        }
    }

    def deleteUser(username: String): Action[AnyContent] = Action {
        val userOpt = userService.removeUser(username)
        userOpt match {
            case Success(user) => Ok
            case Failure(error) => NotFound(error.getMessage())
        }
    }

    def deleteRoom(roomname: String): Action[AnyContent] = Action {
        val roomOpt = roomService.removeRoom(roomname)
        roomOpt match {
            case Success(user) => Ok
            case Failure(error) => NotFound(error.getMessage())
        }
    }
}
