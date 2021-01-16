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
import javafx.util.Pair;
import pathCalculator.PathCalculatorClient;
import sample.Helpers.AlertHelper;
import sample.Helpers.ArrayFlatter;
import sample.StaticClasses.ColorAndHeight;
import sample.StaticClasses.Point;
import simulator.NetworkCommands;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


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
        final String CSV_FILE_LOCATION = "Data/Honolulu.csv"; // TODO: Should input file location from user. until then, from csv.
        Optional<String> fileName = AlertHelper.displayFileDialog(owner, Optional.of(CSV_FILE_LOCATION)).showAndWait();
        if (fileName.isPresent()) {
            if (loadDataFromFile(fileName.get())) {
                ColorAndHeight[][] matrix = model.getColorMap();
                updateGridPane(matrix);
                model.addLocationChangedListener(this::onAirplaneChangedLocation);
            }
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
                MapItemView view = new MapItemView(x, y,
                        (int)map.getWidth() / values[0].length, (int)map.getHeight() / values.length);
                cells[x][y] = view;
                view.setText((int)values[x][y].height + "");
                view.setBackground(new Background(new BackgroundFill(
                        (values[x][y].color), null, null)));
                view.setOnMouseClicked(event -> onMapItemClicked(view, event));
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
                cells[x][y].setBackground(new Background(new BackgroundFill(
                        (values[x][y].color), null, null)));
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
        if (PathCalculatorClient.getInstance().isConnected()) {
            calculatePath(model.getStartPosition(), model.getEndPosition());
        } else {
            redrawGridPane();
        }
    }

    private void calculatePath(Point geoStart, Point geoEnd) {
        model.calculateShortestPathBetween(geoStart, geoEnd).thenAccept(pointsInTheMiddle -> {
            Platform.runLater(()-> {
                redrawGridPane();
                for (Point mapPoint : pointsInTheMiddle) {
                    cells[(int)mapPoint.x][(int)mapPoint.y].markAsViaSquare();
                }
            });
        });
    }

    public void onCalculatePathPressed(ActionEvent actionEvent) {
        Window owner = loadDataButton.getScene().getWindow();
        try {
            Optional<String> defaultHost = Optional.of(PathCalculatorClient.DEFAULT_SERVER_HOST.getHostAddress());
            Optional<Integer> defaultPort = Optional.of(PathCalculatorClient.DEFAULT_PORT);
            Optional<Pair<String, Integer>> result = AlertHelper.displayHostAndPort(owner, defaultHost, defaultPort).showAndWait();
            if (result.isPresent()) {
                PathCalculatorClient.getInstance().connect(result.get().getKey(), result.get().getValue());
                AlertHelper.displayAlert(owner, Alert.AlertType.CONFIRMATION, "OK", "Connection OK");
            }
        } catch (IOException e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR, "NOT GOOD", "Connection to server Failed");
        }
    }

    public void onConnectButtonPressed(ActionEvent actionEvent) {
        Window owner = loadDataButton.getScene().getWindow();
        try {
            Optional<String> defaultServer = Optional.of("localhost");
            Optional<Integer> defaultPort = Optional.of(5402);
            Optional<Pair<String, Integer>> ipAndPort = AlertHelper.displayHostAndPort(owner, defaultServer, defaultPort).showAndWait();
            if (ipAndPort.isPresent()) {
                NetworkCommands.getInstance().connect(ipAndPort.get().getKey(), ipAndPort.get().getValue());
                AlertHelper.displayAlert(owner, Alert.AlertType.CONFIRMATION, "OK", "Connection to simulator OK");
            }
        } catch (IOException e) {
            AlertHelper.displayAlert(owner, Alert.AlertType.ERROR, "Not Good", "Connection to simulator not established");
        }
    }
}
