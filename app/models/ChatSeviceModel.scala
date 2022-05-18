package models

import java.util.UUID


case class User(id: UUID, username: String)
case class NewUser(username: String)

case class Room(id: UUID, roomname: String)
case class NewRoom(roomname: String)

case class Message(roomId: UUID, fromUser: String, msg: String)
case class Wisper(toUser: String, fromUser: String,  msg: String)
case class NewMessage(msg: String, fromUser: String)
case class NewWisper(msg: String, fromUser: String)