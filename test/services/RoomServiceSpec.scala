package services

import org.scalatestplus.play.PlaySpec
import scala.util.Success
import scala.util.Failure
import java.util.UUID

class RoomServiceSpec extends PlaySpec {
    "Room Service" must {    
      "give back an empty list when just created " in {
        val roomService = new RoomService()
        roomService.removeRoom("General")
        roomService.getAll() mustBe(List.empty)
      }

      "give back a list of rooms after his creation" in {
        val roomService = new RoomService()
        roomService.removeRoom("General")
        roomService.addRoom("Recocha")
        roomService.addRoom("Chisme")
        roomService.getAll().size mustBe 2
      }

      "give back a room when exist" in {
        val roomService = new RoomService()
        roomService.removeRoom("General")
        roomService.addRoom("Recocha")
        roomService.addRoom("Chisme") match {
            case Success(room) => roomService.findById(room.id.toString()) mustBe a [Some[_]]
            case Failure(exception) => fail("Should return the created room") 
        }
      }

      "do not return a room when it does not exist" in {
        val roomService = new RoomService()
        roomService.removeRoom("General")
        roomService.addRoom("Recocha")
        roomService.addRoom("Chisme")
        roomService.findById(UUID.randomUUID().toString()) mustBe (None)
      }

      "add a room when not exist" in {
        val roomService = new RoomService()
        roomService.addRoom("Chisme") mustBe a [Success[_]]
      }

      "not add a room when this room already exist" in {
        val roomService = new RoomService()
        roomService.addRoom("Chisme")
        roomService.addRoom("Chisme") mustBe a [Failure[_]]
      }

      "delete a room when is requested" in {
        val roomService = new RoomService()
        roomService.addRoom("Recocha")
        roomService.addRoom("Chisme")
        roomService.removeRoom("Chisme") mustBe a [Success[_]]
      }

      "Do not delete a user when it does not exist" in {
        val roomService = new RoomService()
        roomService.addRoom("Recocha")
        roomService.addRoom("Chisme")
        roomService.removeRoom("Trabajo") mustBe a [Failure[_]]
      }
  }
  
}
