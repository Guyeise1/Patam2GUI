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

    public void loadMapFromFile(String fileName) throws IOException, VerifyError {
        List<List<Integer>> heigths = new ArrayList<>();
        String row;
        int max_height = 0;

        try(BufferedReader csvReader = new BufferedReader(new FileReader(fileName))) {
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
        final Point POINT_FROM_FILE = new Point(1, 1);
        setStartPosition(POINT_FROM_FILE);
        setCurrentLocation(new Point(-1, -1));
        this.map = Optional.of(buildMatrixFromList(heigths, max_height));
        listenForAirplaneChanged();
    }

    public void addLocationChangedListener(Consumer<Point> locationChangedListener) {
        locationChangedListeners.add(locationChangedListener);
    }

    public void removeLocationChangedListener(Consumer<Point> locationChangedListener) {
        locationChangedListeners.remove(locationChangedListener);
    }

    public void notifyLocationChanged(Point newLocation) {
        for (Consumer<Point> locationListeners : this.locationChangedListeners) {
            locationListeners.accept(newLocation);
        }
    }

    public void listenForAirplaneChanged() {
        shouldListenToAirplaneChanges = true;
        new Thread(this::checkForAirplaneChangedLocation).start();
    }

    private void checkForAirplaneChangedLocation() {
        try {
            Point airplaneLocation = fetchAirplaneLocation();
            if (!airplaneLocation.equals(getCurrentPlaneLocation())) {
                setCurrentLocation(airplaneLocation);
                notifyLocationChanged(airplaneLocation);
            }
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Point fetchAirplaneLocation() {
        //TODO: implement how to get location
        return new Point(1, 3);
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
            double percentage = 1.0 * height / maxHeight;
            percentage = 1 - percentage;
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

    public void setStartPosition(Point point) {
        this.currentPosition = Optional.of(point);
    }


    public double getSquareSize() {
        return squareSize.orElseThrow();
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


    public void setCurrentLocation(Point currentLocation) {
        this.currentPosition = Optional.of(new Point(currentLocation.x, currentLocation.y));
    }


}
