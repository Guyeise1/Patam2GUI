package sample.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import sample.Helpers.AlertHelper;
import sample.StaticClasses.ColorAndHeight;
import sample.StaticClasses.Point;

import java.io.IOException;


public class MapController {

    @FXML
    private Button loadDataButton;

    @FXML
    private GridPane map;

    MapItemView[][] cells;

    private MapModel model = new MapModel();

    @FXML
    private void onLoadButtonPressed(ActionEvent event) {
        Window owner = loadDataButton.getScene().getWindow();
        final String CSV_FILE_LOCATION = "matrix.csv"; // TODO: Should input file location from user. until then, from csv.
        try {
            model.loadMapFromFile(CSV_FILE_LOCATION);
            ColorAndHeight[][] matrix = model.getColorMap();
            updateGridPane(matrix);
            model.addLocationChangedListener(this::onAirplaneChangedLocation);
        } catch (IOException e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR,
                    "Error Reading File", "could not open file " + CSV_FILE_LOCATION + " for reading");
        } catch (VerifyError e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR,
                    "Error Reading File", "the file" + CSV_FILE_LOCATION + " is not proper heights table");
        }
    }

    private void updateGridPane(ColorAndHeight[][] values) {
        map.getChildren().clear();
        cells = new MapItemView[values.length][];
        for (int x = 0; x < values.length; x ++) {
            cells[x] = new MapItemView[values[0].length];
            for (int y = 0; y < values[0].length; y++) {
                MapItemView view = new MapItemView(x, y);
                cells[x][y] = view;
                view.setText(values[x][y].height + "");
                view.setBackground(new Background(new BackgroundFill(
                        (values[x][y].color), null, null)));
                view.setOnMouseClicked(event -> onMapItemClicked(view, event));
                view.setMaxWidth(map.getWidth() / values[0].length);
                view.setMinHeight(map.getHeight() / values.length);
                map.add(view, y, x);
            }
        }
    }

    private MapItemView findMapItemViewByPoint(Point point) {
        return cells[0][0];
    }

    public void onAirplaneChangedLocation(Point newLocation) {
        findMapItemViewByPoint(newLocation).displayAirplane();
    }

    @FXML
    private void initialize() {
        assert loadDataButton != null : "fx:id=\"loadDataButton\" was not injected: check "
                + "your FXML file.";
    }

    public void onMapItemClicked(MapItemView view, MouseEvent event) {
    }

}
