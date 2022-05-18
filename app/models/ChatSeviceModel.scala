package models

import scala.collection.mutable
import java.util.UUID

object ChatSeviceModel {
    
    private val users = mutable.Map[String, UUID]()

    def createUser(username: String): Boolean = {
        if (users.contains(username)) false else true

    }

}
