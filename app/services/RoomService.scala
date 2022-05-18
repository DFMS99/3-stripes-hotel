package services

import models._
import scala.collection.mutable
import scala.util.Try
import scala.util.Failure
import java.util.UUID
import scala.util.Success

class RoomService {
  private val roomsDB = new mutable.ListBuffer[Room]()
  roomsDB += Room(UUID.randomUUID(), "General")


  def getAll(): List[Room] = roomsDB.toList

  def findById(id: String): Option[Room] = roomsDB.find(_.id == UUID.fromString(id))

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

    def removeRoom(roomname: String): Try[Room] = {
        roomsDB.find(_.roomname == roomname) match {
            case Some(room) => {
                roomsDB -= room
                Success(room)
            }
            case None => Failure(new Exception("The room doesn't exist")) // The user already exists 
        }
    }
}
