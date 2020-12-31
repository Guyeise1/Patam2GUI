package sample.Map;

import javafx.scene.control.Alert;
import javafx.stage.*;

public class AlertHelper {

    public static void displayAlert(Window owner, Alert.AlertType type, String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.initOwner(owner);
        alert.show();
    }
}
