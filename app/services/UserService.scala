package services

import scala.collection.mutable
import models.User
import scala.util.Try
import scala.util.Failure
import java.util.UUID
import scala.util.Success
import com.typesafe.sslconfig.ssl.FakeChainedKeyStore

class UserService {
    private val usersDB = new mutable.ListBuffer[User]()

    def getAll(): List[User] = usersDB.toList

    def findByUsername(username: String): Option[User] = usersDB.find(_.username == username) 

    def addUser(username: String): Try[User] = {
        usersDB.find(_.username == username) match {
            case Some(user) => Failure(new Exception("The user already exists!!!!!")) // The user already exists
            case None => {
                val newUser = User(UUID.randomUUID(), username)
                usersDB += newUser
                Success(newUser)
            }
        }
    }

    def removeUser(username: String): Try[User] = {
        usersDB.find(_.username == username) match {
            case Some(user) => {
                usersDB -= user
                Success(user)
            }
            case None => Failure(new Exception("The user doesn't exist")) // The user already exists 
        }
    }
}
