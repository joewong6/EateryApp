package ch.makery.eatery.view

import ch.makery.eatery.model.Restaurant
import ch.makery.eatery.MainApp
import scalafx.scene.control.{TableView, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{StringProperty}
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scala.util.{Failure, Success}

@sfxml
class EateryOverviewController(

    private val restaurantTable : TableView[Restaurant],
    private val NameColumn : TableColumn[Restaurant, String],

    private val NameLabel : Label,
    private val foodTypeLabel : Label,
    private val priceRangeLabel : Label,
    private val overallRatingLabel : Label,
    private val cityLabel : Label,
    private val postalCodeLabel : Label

    ) {

        private def showRestaurantDetails (restaurant : Option[Restaurant]) = {

            restaurant match {
            case Some(restaurant) =>

            // Fill the labels with info from the restaurant object.
            NameLabel.text <== restaurant.name
            foodTypeLabel.text <== restaurant.foodType
            priceRangeLabel.text <== restaurant.priceRange
            overallRatingLabel.text <== restaurant.overallRating
            cityLabel.text <== restaurant.city;
            postalCodeLabel.text = restaurant.postalCode.value.toString

            case None =>
            // When restaurant is null, remove all the text.
            NameLabel.text = ""
            foodTypeLabel.text = ""
            priceRangeLabel.text = ""
            overallRatingLabel.text = ""
            cityLabel.text = ""
            postalCodeLabel.text= ""
            }
        }

    // initialize the table
    restaurantTable.items = MainApp.restaurantData

    // initialize the restaurant name in table
    NameColumn.cellValueFactory = {_.value.name}

    // initialize every variable empty to prevent clash with old
    showRestaurantDetails(None);

    restaurantTable.selectionModel().selectedItem.onChange(
        (_, _, newValue) => showRestaurantDetails(Some(newValue))
    )

    def handleDeleteRestaurant( action : ActionEvent) = {
        val selectedIndex = restaurantTable.selectionModel().selectedIndex.value
        val selectedRestaurant = restaurantTable.selectionModel().selectedItem.value

        if (selectedIndex >= 0) {
            selectedRestaurant.delete() match {
                case Success(x) => 
                    restaurantTable.items().remove(selectedIndex);
                 case Failure(e) =>
                    val alert = new Alert(Alert.AlertType.Warning) {
                        initOwner(MainApp.stage)
                        title = "Failed to delete"
                        headerText = "Error occured "
                        contentText = "Database failed to make the changes"
                    }.showAndWait()                
            }
        } else {
            val alert = new Alert(AlertType.Warning) {
                initOwner(MainApp.stage)
                title = "No Selection"
                headerText = "No Existing Restaurant Selected"
                contentText = "Please Select a suitable Restaurant in the Table."
            }.showAndWait()
        }
    }

    def handleNewRestaurant(action : ActionEvent) = {
        val restaurant = new Restaurant("")
        val applyClicked = MainApp.showRestaurantModifyDialog(restaurant);
            if (applyClicked) {
                restaurant.save() match {
                    case Success(x) =>
                        MainApp.restaurantData += restaurant
                    case Failure(e) =>
                        val alert = new Alert(Alert.AlertType.Warning) {
                            initOwner(MainApp.stage)
                            title = "Failed to save"
                            headerText = "Database Error"
                            contentText = "Database failed to make the changes"
                        }.showAndWait()
                }
            }

    }

    def handleModifyRestaurant(action : ActionEvent) = {
        val selectedRestaurant = restaurantTable.selectionModel().selectedItem.value
        if (selectedRestaurant != null) {
            val applyClicked = MainApp.showRestaurantModifyDialog(selectedRestaurant)

            if (applyClicked) {
                selectedRestaurant.save() match {
                    case Success(x) =>
                        showRestaurantDetails(Some(selectedRestaurant))
                    case Failure(e) =>
                        val alert = new Alert(Alert.AlertType.Warning) {
                            initOwner(MainApp.stage)
                            title = "Failed to save"
                            headerText = "Error occured in Database"
                            contentText = "Database failed to save the changes"
                        }.showAndWait()
                }
            }

        } else {
            // Nothing selected.
            val alert = new Alert(Alert.AlertType.Warning){
                initOwner(MainApp.stage)
                title = "No Selection"
                headerText = "No Restaurant Selected"
                contentText = "Please select a restaurant from the table."
            }.showAndWait()
        }
    }

}