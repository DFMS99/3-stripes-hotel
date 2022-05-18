package services

import org.scalatestplus.play.PlaySpec
import com.typesafe.sslconfig.ssl.FakeChainedKeyStore
import models.User
import java.util.UUID
import scala.util.Success
import scala.util.Failure
import org.junit.Test


class UserServiceSpec extends PlaySpec {
  "User Service" must {    
      "give back an empty list when just created " in {
        val userService = new UserService()
        userService.getAll() mustBe(List.empty)
      }

      "give back a list of users after users registration" in {
        val userService = new UserService()
        userService.addUser("Daniel")
        userService.addUser("Juan")
        userService.getAll().size mustBe 2
      }

      "give back a user " in {
        val userService = new UserService()
        userService.addUser("Daniel")
        userService.addUser("Juan")
        userService.findByUsername("Daniel") mustBe a [Some[_]]
      }

      "do not return a user when it does not exist" in {
        val userService = new UserService()
        userService.addUser("Daniel")
        userService.addUser("Juan")
        userService.findByUsername("Victor") mustBe (None)
      }

      "add a user when not exist" in {
        val userService = new UserService()
        userService.addUser("Daniel") mustBe a [Success[_]]
      }

      "not add a user when this user already exist" in {
        val userService = new UserService()
        userService.addUser("Daniel")
        userService.addUser("Daniel") mustBe a [Failure[_]]
      }

      "delete a user when is requested" in {
        val userService = new UserService()
        userService.addUser("Daniel")
        userService.addUser("Juan")
        userService.removeUser("Daniel") mustBe a [Success[_]]
      }

      "Do not delete a user when it does not exist" in {
        val userService = new UserService()
        userService.addUser("Daniel")
        userService.addUser("Juan")
        userService.removeUser("Victor") mustBe a [Failure[_]]
      }
  }
}
