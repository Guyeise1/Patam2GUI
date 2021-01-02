package sample.Map;

import javafx.application.Platform;
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
import java.util.NoSuchElementException;


public class MapController {

    @FXML
    private Button loadDataButton;

    @FXML
    private GridPane map;

    MapItemView[][] cells;

    private MapModel model = new MapModel();

    @FXML
    private void onLoadButtonPressed(ActionEvent event) {
        final String CSV_FILE_LOCATION = "Data/Honolulu.csv"; // TODO: Should input file location from user. until then, from csv.
        if(loadDataFromFile(CSV_FILE_LOCATION)) {
            ColorAndHeight[][] matrix = model.getColorMap();
            updateGridPane(matrix);
            model.addLocationChangedListener(this::onAirplaneChangedLocation);
        }

    }

    private boolean loadDataFromFile(String fileName) {
        Window owner = loadDataButton.getScene().getWindow();
        try {
            model.loadMapFromFile(fileName);
        } catch (IOException e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR,
                    "Error Reading File", "could not open file " + fileName + " for reading");
            return false;
        } catch (VerifyError e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR,
                    "Error Reading File", "the file" + fileName + " is not proper heights table");
            return false;
        }

        return true;
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
                view.setMinWidth(map.getWidth() / values[0].length);
                view.setMinHeight(map.getHeight() / values.length);
                map.add(view, y, x);
            }
        }
        findMapItemViewByPoint(model.getCurrentPlaneLocation()).displayAirplane();
    }

    public void redrawGridPane() {
        ColorAndHeight[][] values = model.getColorMap();
        for (int x = 0; x < values.length; x ++) {
            for (int y = 0; y < values[0].length; y++) {
                cells[x][y].clear();
            }
        }
        try {
            findMapItemViewByPoint(model.getEndPosition()).displayX();
        } catch (NoSuchElementException e){}
        try {
            findMapItemViewByPoint(model.getCurrentPlaneLocation()).displayAirplane();
        } catch (NoSuchElementException e){}
    }

    private MapItemView findMapItemViewByPoint(Point point) {
        double x = (point.x - model.getStartPosition().x) / model.getSquareSize();
        double y = (point.y - model.getStartPosition().y) / model.getSquareSize();
        return cells[(int)x][(int)y];
    }

    public void onAirplaneChangedLocation(Point newLocation) {
        Platform.runLater(this::redrawGridPane);
    }

    @FXML
    private void initialize() {
        assert loadDataButton != null : "fx:id=\"loadDataButton\" was not injected: check "
                + "your FXML file.";
    }

    public void onMapItemClicked(MapItemView view, MouseEvent event) {
        // The destination is considered to be the middle of cell, hence the 0.5
        double endX = ( view.column + 0.5 ) * model.getSquareSize() + model.getStartPosition().x;
        double endY = ( view.row + 0.5 ) * model.getSquareSize() + model.getStartPosition().y;
        model.setEndPosition(new Point(endX, endY));
        redrawGridPane();
    }
}
