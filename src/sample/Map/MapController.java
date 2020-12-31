package sample.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Window;
import sample.Helpers.AlertHelper;
import sample.StaticClasses.ItemInMap;

import java.io.IOException;


public class MapController {

    @FXML
    private Button loadDataButton;

    @FXML
    private GridPane map;

    private MapModel model = new MapModel();

    @FXML
    private void onLoadButtonPressed(ActionEvent event) {
        Window owner = loadDataButton.getScene().getWindow();
        final String CSV_FILE_LOCATION = "/Users/orsh/Documents/matrix.csv";
        try {
            ItemInMap[][] matrix = model.createMapFromFile(CSV_FILE_LOCATION);
            updateGridPane(matrix);
        } catch (IOException e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR,
                    "Error Reading File", "could not open file " + CSV_FILE_LOCATION + " for reading");
        } catch (VerifyError e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR,
                    "Error Reading File", "the file" + CSV_FILE_LOCATION + " is not proper heights table");
        }
    }

    private void updateGridPane(ItemInMap[][] values) {
        map.getChildren().clear();
        for (int x = 0; x < values.length; x ++) {
            for (int y = 0; y < values[0].length; y++) {
                MapItemView view = new MapItemView(x, y);
                view.setText(values[x][y].height + "");
                view.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(values[x][y].color), null, null)));
                map.add(view, y, x);
            }
        }
    }

    @FXML
    void initialize() {
        assert loadDataButton != null : "fx:id=\"loadDataButton\" was not injected: check "
                + "your FXML file.";
    }

}
