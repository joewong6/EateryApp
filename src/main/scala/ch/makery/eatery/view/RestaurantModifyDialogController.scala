package ch.makery.eatery.view

import ch.makery.eatery.model.Restaurant
import ch.makery.eatery.MainApp
import scalafx.scene.control.{TextField, TableColumn, Label, Alert}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class RestaurantModifyDialogController (

private val nameField : TextField,
private val foodTypeField : TextField,
private val priceRangeField : TextField,
private val overallRatingField : TextField,
private val cityField : TextField,
private val postalCodeField : TextField
){

    var dialogStage : Stage = null
    private var _restaurant : Restaurant = null
    var applyClicked = false

    def restaurant = _restaurant

    def restaurant_=(x : Restaurant) {

        _restaurant = x

        nameField.text = _restaurant.name.value
        foodTypeField.text = _restaurant.foodType.value
        priceRangeField.text = _restaurant.priceRange.value
        overallRatingField.text = _restaurant.overallRating.value
        cityField.text = _restaurant.city.value
        postalCodeField.text= _restaurant.postalCode.value.toString

    }
    // method to apply modification
    def handleApply(action :ActionEvent){
        if (isInputValid()) {

            _restaurant.name <== nameField.text
            _restaurant.foodType <== foodTypeField.text
            _restaurant.priceRange <== priceRangeField.text
            _restaurant.overallRating <== overallRatingField.text
            _restaurant.city <== cityField.text
            _restaurant.postalCode.value = postalCodeField.getText().toInt

            applyClicked = true;

            dialogStage.close()
        }
    }

    // method to cancel modification
    def handleCancel(action :ActionEvent) {
        dialogStage.close();
    }

    def nullChecking (x : String) = x == null || x.length == 0

    def isInputValid() : Boolean = {

        var errorMessage = ""

        if (nullChecking(nameField.text.value))

            errorMessage += "No valid name!\n"

        if (nullChecking(foodTypeField.text.value))

            errorMessage += "No valid food type!\n"

        if (nullChecking(priceRangeField.text.value))

            errorMessage += "No valid price range!\n"

        if (nullChecking(postalCodeField.text.value))

            errorMessage += "No valid postal code!\n"

        else {
            try {

                Integer.parseInt(postalCodeField.getText());

            } catch {

                case e : NumberFormatException =>

                errorMessage += "No valid postal code (must be an integer)!\n"
            }
        }

        if (nullChecking(cityField.text.value))

            errorMessage += "No valid city!\n"

        if (errorMessage.length() == 0) {

            return true;

        } else {

        // Show the error message.

        val alert = new Alert(Alert.AlertType.Error){

            initOwner(dialogStage)

            title = "Invalid Fields"

            headerText = "Please correct invalid fields"

            contentText = errorMessage

        }.showAndWait()

        return false;

        }

    }

}