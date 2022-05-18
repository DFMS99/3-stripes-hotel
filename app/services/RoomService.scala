package services

import models._
import scala.collection.mutable
import scala.util.Try
import scala.util.Failure
import java.util.UUID
import scala.util.Success

class RoomService {
  private val roomsDB = new mutable.ListBuffer[Room]()


  def getAll(): List[Room] = ???

  def findById(): Option[Room] = ???

  def addRoom (roomname: String): Try[Room] = {
      roomsDB.find(_.roomname == roomname) match {
            case Some(room) => Failure(new Exception("The user already exists")) // The user already exists
            case None => {
                val newRoom = Room(UUID.randomUUID(), roomname)
                roomsDB += newRoom
                Success(newRoom)
            }
        }
    }

    def removeRoom(id: String): Try[Room] = {
        roomsDB.find(_.id == id) match {
            case Some(room) => {
                roomsDB -= room
                Success(room)
            }
            case None => Failure(new Exception("The user doesn't exist")) // The user already exists 
        }
    }
}
