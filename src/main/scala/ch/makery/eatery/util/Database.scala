package ch.makery.eatery.util

import scalikejdbc._
import ch.makery.eatery.model.Restaurant

// database trait for obj database extension
trait Database {
    val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
    val dbURL = "jdbc:derby:myDB;create=true;";

    // initialization of JDBC driver & connection pool
    Class.forName(derbyDriverClassname)
    ConnectionPool.singleton(dbURL, "me", "mine")

    // ad-hoc session provider on the REPL
    implicit val session = AutoSession

}

object Database extends Database{
    def setupDB() = {

        // for table not exist
        if (!hasDBInitialize)
            Restaurant.initializeTable()

    }

    def hasDBInitialize : Boolean = {

        DB getTable "Restaurant" match {

            case Some(x) => true
            case None => false

        }
    }
}