package ch.makery.eatery.model

import scalafx.beans.property.{StringProperty, ObjectProperty}
import ch.makery.eatery.util.Database
import scalikejdbc._
import scala.util.{ Try, Success, Failure }

class Restaurant (val nameD : String ) extends Database{

    def this() = this(null)
    var name = new StringProperty(nameD)
    var foodType = new StringProperty("any food type")
    var priceRange = new StringProperty(" any price range")
    var overallRating = new StringProperty(" average rating")
    var city = new StringProperty(" any city")
    var postalCode = ObjectProperty[Int](1234)

    def save() : Try[Int] = {
        if (!(isExist)) {
            Try(DB autoCommit { implicit session =>
            sql"""
                insert into restaurant (name, foodType,
                priceRange, overallRating, city, postalCode) values
                (${name.value}, ${foodType.value}, ${priceRange.value}, ${overallRating.value},
                ${city.value}, ${postalCode.value})
            """.update.apply()
            })
        } else {
            Try(DB autoCommit { implicit session =>
            sql"""
                update restaurant
                set
                name = ${name.value} ,
                foodType = ${foodType.value},
                priceRange = ${priceRange.value},
                overallRating = ${overallRating.value},
                city = ${city.value},
                postalCode = ${postalCode.value}
                    where name = ${name.value}
            """.update.apply()
            })
        }
    }

    def delete() : Try[Int] = {
        if (isExist) {
            Try(DB autoCommit { implicit session =>
            sql"""
                delete from restaurant where
                name = ${name.value}
            """.update.apply()
            })
        } else
            throw new Exception("Restaurant does not Exists in Database")

    }

    def isExist : Boolean = {

        DB readOnly { implicit session =>
            sql"""
                select * from restaurant where
                name = ${name.value}
            """.map(rs => rs.string("name")).single.apply()

        } match {
            case Some(x) => true
            case None => false

        }

    }
}

object Restaurant extends Database{

    def apply (
        nameD : String,
        foodTypeD : String,
        priceRangeD : String,
        overallRatingD : String,
        cityD : String,
        postalCodeD : Int
    ) : Restaurant = 
    {
        new Restaurant(nameD) {
            foodType.value = foodTypeD
            priceRange.value = priceRangeD
            overallRating.value = overallRatingD
            city.value = cityD
            postalCode.value = postalCodeD
        }
    }

    def initializeTable() = {
        DB autoCommit { implicit session =>
            sql"""
                create table restaurant (
                    id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                    name varchar(80),
                    foodType varchar(64),
                    priceRange varchar(64),
                    overallRating varchar(64),
                    city varchar(100),
                    postalCode int
                )
            """.execute.apply()
        }
    }

    def getAllRestaurants : List[Restaurant] = {
        DB readOnly { implicit session =>

        sql"select * from restaurant".map(rs => Restaurant(rs.string("name"),
        rs.string("foodType"),rs.string("priceRange"),
        rs.string("overallRating"),rs.string("city"),rs.int("postalCode") )).list.apply()
        }
    }
}

