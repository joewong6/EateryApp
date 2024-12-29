package ch.makery.eatery

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import ch.makery.eatery.model.Restaurant
import ch.makery.eatery.view.RestaurantModifyDialogController
import scalafx.stage.{ Stage, Modality }
import ch.makery.eatery.util.Database
import scalafx.scene.image.Image

object MainApp extends JFXApp {

    //initialize restaurant database
    Database.setupDB()

    val restaurantData = new ObservableBuffer[Restaurant]()

    //store all restaurants into restaurantData array
    restaurantData ++= Restaurant.getAllRestaurants

    // test data
    // restaurantData += new Restaurant("Texas Chicken")
    // restaurantData += new Restaurant("Rock Cafe")
    // restaurantData += new Restaurant("Garlic Chicken Rice")
    // restaurantData += new Restaurant("Shaz")
    // restaurantData += new Restaurant("After Black")
    // restaurantData += new Restaurant("MyBurgerLab")
    // restaurantData += new Restaurant("Family Mart")
    // restaurantData += new Restaurant("Sushi Zamai")
    // restaurantData += new Restaurant("Mcdonald") 


    // transform path of RootLayout.fxml to URI for resource location.
    val rootResource = getClass.getResource("view/RootLayout.fxml")

    // initialize the loader object.
    val loader = new FXMLLoader(rootResource, NoDependencyResolver)

    // Load root layout from fxml file.
    loader.load();

    // retrieve the root component BorderPane from the FXML
    val roots = loader.getRoot[jfxs.layout.BorderPane]

    //get css style sheet
    val cssResource = getClass.getResource("view/Eatery.css")
    roots.stylesheets = List(cssResource.toExternalForm)

    // initialize stage
    stage = new PrimaryStage {
        title = "Eatery"
        icons += new Image("file:resources/images/Eatery_icon.png")
        scene = new Scene {
            root = roots

        }

    }

    // method for display the main eatery view
    def showEateryOverview() = {
        val resource = getClass.getResource("view/MainEateryView.fxml")
        val loader = new FXMLLoader(resource, NoDependencyResolver)
        loader.load();
        val roots = loader.getRoot[jfxs.layout.AnchorPane]
        this.roots.setCenter(roots)

    }

    // call to display Eatery when app start
    showEateryOverview()

    
    def showRestaurantModifyDialog(restaurant: Restaurant): Boolean = {
        val resource = getClass.getResourceAsStream("view/RestaurantModifyDialog.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)

        loader.load(resource);
        
        val roots2 = loader.getRoot[jfxs.Parent]
        val control = loader.getController[RestaurantModifyDialogController#Controller]
        roots2.stylesheets = List(cssResource.toExternalForm)
        val dialog = new Stage() {
            title = "Modify"
            icons += new Image("file:resources/images/Eatery_icon.png")
            initModality(Modality.APPLICATION_MODAL)
            initOwner(stage)

            scene = new Scene {
                root = roots2
            }
        }

        control.dialogStage = dialog
        control.restaurant = restaurant

        dialog.showAndWait()
        control.applyClicked

    }

}