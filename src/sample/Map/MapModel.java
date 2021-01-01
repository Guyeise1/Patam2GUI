package sample.Map;

import javafx.scene.paint.Color;
import sample.StaticClasses.ColorAndHeight;
import sample.StaticClasses.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapModel {

    private Optional<Point> startPosition;
    private Optional<Point> currentPosition;
    private Optional<Point> endPosition;

    private Optional<Double> squareSize;

    private Optional<ColorAndHeight[][]>  map;

    private List<Consumer<Point>> locationChangedListeners;

    private boolean shouldListenToAirplaneChanges;

    public MapModel() {
        this.startPosition = Optional.empty();
        this.squareSize = Optional.empty();
        this.map = Optional.empty();
        this.endPosition = Optional.empty();
        locationChangedListeners = new ArrayList<>();
        shouldListenToAirplaneChanges = false;
    }

    //// Loading data from files

    public void loadMapFromFile(String fileName) throws IOException, VerifyError {
        List<List<Integer>> heigths = new ArrayList<>();
        String row;
        int max_height = 0;

        try(BufferedReader csvReader = new BufferedReader(new FileReader(fileName))) {

            // TODO: Implement Retviving points from file.
            final Point POINT_FROM_FILE = new Point(1, 1);
            setStartPosition(POINT_FROM_FILE);
            setCurrentLocation(POINT_FROM_FILE);

            // TODO: Implement Retviving SQUARE SIZE from file
            final double SQUARE_SIZE_FROM_FILE = 1;
            setSquareSize(SQUARE_SIZE_FROM_FILE);

            while ((row = csvReader.readLine()) != null) {
                List<Integer> heightsRow = Stream
                        .of(row.split(","))
                        .filter(number -> !number.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                max_height = Math.max(max_height, Collections.max(heightsRow));
                heigths.add(heightsRow);
            }
        }
        this.map = Optional.of(buildMatrixFromList(heigths, max_height));
        listenForAirplaneChanged();
    }

    private ColorAndHeight[][] buildMatrixFromList(List<List<Integer>> heigths, int maxHeight) throws VerifyError {
        verifyMatrix(heigths);
        int d_1 = heigths.size();
        int d_2 = heigths.get(0).size();
        ColorAndHeight[][] matrix = new ColorAndHeight[d_1][d_2];
        Function<Integer, Color> heightToColor = buildColorFromHeightFunction(maxHeight);
        for (int x = 0; x < d_1; x++) {
            matrix[x] = new ColorAndHeight[d_2];
            for (int y = 0; y < d_2; y++) {
                int height = heigths.get(x).get(y);
                matrix[x][y] = new ColorAndHeight(heightToColor.apply(height), height);
            }
        }
        return matrix;
    }

    private void verifyMatrix(List<List<Integer>> heigths) throws VerifyError {
        if (heigths == null || heigths.isEmpty()) {
            throw new VerifyError("No heights found");
        }
        int d_2_size = heigths.get(0).size();
        if(heigths.stream().anyMatch(listHeights -> listHeights.size() != d_2_size)) {
            throw new VerifyError("Not all rows are at the same size");
        }
    }

    private Function<Integer, Color> buildColorFromHeightFunction(int maxHeight) {
        return height ->  {
            double percentage = 1.0 - 1.0 * height / maxHeight;
            int red = 255;
            int green = 255;
            if (percentage >= 0 && percentage <= 0.5) {
                green = (int)( 510 * percentage);
            } else if (percentage > 0.5 && percentage <= 1) {
                red = (int)(-510 * percentage + 510);
            }
            return Color.rgb(red,green, 0);
        };
    }

    ////// Plane Location Handlers

    public void listenForAirplaneChanged() {
        shouldListenToAirplaneChanges = true;
        new Thread(this::checkForAirplaneChangedLocation).start();
    }

    private void checkForAirplaneChangedLocation() {
        try {
            while (shouldListenToAirplaneChanges) {
                fetchAirplaneLocation();
                Point airplaneLocation = getCurrentPlaneLocation();
                if (!airplaneLocation.equals(getCurrentPlaneLocation())) {
                    setCurrentLocation(airplaneLocation);
                    notifyLocationChanged(airplaneLocation);
                }
                Thread.sleep(250);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fetchAirplaneLocation() {
        //TODO: implement how to get location
        setCurrentLocation(new Point(2, 2));
    }

    ////// Event Handlers

    public void addLocationChangedListener(Consumer<Point> locationChangedListener) {
        synchronized (this) {
            locationChangedListeners.add(locationChangedListener);
        }
    }

    public void removeLocationChangedListener(Consumer<Point> locationChangedListener) {
        synchronized (this) {
            locationChangedListeners.remove(locationChangedListener);
        }
    }

    public void notifyLocationChanged(Point newLocation) {
        synchronized (this) {
            for (Consumer<Point> locationListeners : this.locationChangedListeners) {
                locationListeners.accept(newLocation);
            }
        }
    }

    ////// Getters and setters

    public void setStartPosition(Point point) {
        this.startPosition = Optional.of(point);
    }

    public double getSquareSize() {
        return squareSize.orElseThrow();
    }

    public Point getStartPosition() {
        return startPosition.orElseThrow();
    }

    public Point getCurrentPlaneLocation() {
        return currentPosition.orElseThrow();
    }

    public ColorAndHeight[][] getColorMap() {
        return map.orElseThrow();
    }

    public void setEndPosition(Point end) {
        this.endPosition = Optional.of(end);
    }

    public Point getEndPosition() {
        return endPosition.orElseThrow();
    }

    public void setCurrentLocation(Point currentLocation) {
        this.currentPosition = Optional.of(new Point(currentLocation.x, currentLocation.y));
    }

    public void setSquareSize(double squareSize) {
        this.squareSize = Optional.of(squareSize);
    }
}
