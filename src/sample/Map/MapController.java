package sample.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Window;


public class MapController {

    @FXML
    private Button loadDataButton;

    @FXML
    private void onLoadButtonPressed(ActionEvent event) {
        Window owner = loadDataButton.getScene().getWindow();
        AlertHelper.displayAlert(owner, Alert.AlertType.INFORMATION, "javaFx", "onLoadButton works");
    }

    @FXML
    void initialize() {
        assert loadDataButton != null : "fx:id=\"loadDataButton\" was not injected: check "
                + "your FXML file.";
    }

}
