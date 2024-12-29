package ch.makery.eatery.view

import ch.makery.eatery.MainApp.stage
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class RootController() {
    def quit (action :ActionEvent) {
        stage.close();
    }
}