package sample.Map;

import javafx.scene.paint.Color;
import pathCalculator.PathCalculatorClient;
import sample.Helpers.ArrayFlatter;
import sample.StaticClasses.ColorAndHeight;
import sample.StaticClasses.Point;
import simulator.NetworkCommands;
import simulator.Parameters;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static simulator.Parameters.SimulatorParam.*;

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
        try {
            NetworkCommands.getInstance().connect("46.116.232.105", 5402);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //// Loading data from files

    public void loadMapFromFile(String fileName) throws IOException, VerifyError {
        List<List<Double>> heigths = new ArrayList<>();
        String row;
        int maxCount = 0;

        try(BufferedReader csvReader = new BufferedReader(new FileReader(fileName))) {

            // TODO: Implement Retviving points from file.
            //final Point POINT_FROM_FILE = new Point(1, 1);

            String firstLine = csvReader.readLine();
            String[] quardinates = firstLine.split(",",3);
            double x = Double.parseDouble(quardinates[0]);
            double y = Double.parseDouble(quardinates[1]);
            setStartPosition(new Point(x,y));
            Parameters.setDoubleValue(LONGITUDE_DEG, x);
            Parameters.setDoubleValue(LATITUDE_DEG, y);
            setCurrentLocation(getStartPosition());

            // TODO: Implement Retviving SQUARE SIZE from file
            String secondLine = csvReader.readLine();
            String[] squareSizeArray = firstLine.split(",", 2);
            //final double SQUARE_SIZE_FROM_FILE = 1;
            setSquareSize(Double.parseDouble(squareSizeArray[0]));

            while ((row = csvReader.readLine()) != null) {
                List<Double> heightsRow = Stream
                        .of(row.split(","))
                        .filter(number -> !number.isEmpty())
                        .map(Double::parseDouble)
                        .collect(Collectors.toList());
                maxCount = Math.max(maxCount, heightsRow.size());
                heigths.add(heightsRow);
            }
        }
        this.map = Optional.of(buildMatrixFromList(heigths, maxCount));
        listenForAirplaneChanged();
    }

    private ColorAndHeight[][] buildMatrixFromList(List<List<Double>> heigths, int maxCount) throws VerifyError {
        verifyMatrix(heigths, maxCount);
        heigths = ArrayFlatter.decreaseMatrix(heigths);
        double minHeight = Double.MAX_VALUE, maxHeight = Double.MIN_VALUE;
        int d_1 = heigths.size();
        int d_2 = heigths.get(0).size();
        for (int x = 0; x < d_1; x++) {
            for (int y = 0; y < d_2; y++) {
                double height = heigths.get(x).get(y);
                minHeight = Math.min(height, minHeight);
                maxHeight = Math.max(height, maxHeight);
            }
        }
        ColorAndHeight[][] matrix = new ColorAndHeight[d_1][d_2];
        Function<Double, Color> heightToColor = buildColorFromHeightFunction(minHeight, maxHeight);
        for (int x = 0; x < d_1; x++) {
            matrix[x] = new ColorAndHeight[d_2];
            for (int y = 0; y < d_2; y++) {
                double height = heigths.get(x).get(y);
                matrix[x][y] = new ColorAndHeight(heightToColor.apply(height), height);
            }
        }
        return matrix;
    }

    private void verifyMatrix(List<List<Double>> heigths, int maxCount) throws VerifyError {
        if (heigths == null || heigths.isEmpty()) {
            throw new VerifyError("No heights found");
        }
        final double DEFAULT_HEIGHT = 0;
        for (int i = 0; i < heigths.size(); i ++) {
            for (int diff = maxCount - heigths.get(i).size(); diff > 0; diff--) {
                heigths.get(i).add(DEFAULT_HEIGHT);
            }
        }
//        //int d_2_size = heigths.get(0).size();
//        if(heigths.stream().anyMatch(listHeights -> listHeights.size() != d_2_size)) {
//            throw new VerifyError("Not all rows are at the same size");
//        }
    }

    private Function<Double, Color> buildColorFromHeightFunction(double minHeight, double maxHeight) {
        return height ->  {
            double percentage = (maxHeight - height) / (maxHeight - minHeight);
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
                Point airplaneLocation = fetchAirplaneLocation();
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

    private int countFetch = 0;

    private Point fetchAirplaneLocation() {
        double x = Parameters.getDoubleValue(LONGITUDE_DEG).orElseThrow(() -> new NoSuchElementException("Error getting the x axis"));
        double y = Parameters.getDoubleValue(Parameters.SimulatorParam.LATITUDE_DEG).orElseThrow(() -> new NoSuchElementException("Error getting the y axis"));
        return new Point(x,y);
    }

    public CompletableFuture<List<Point>> calculateShortestPathBetween(Point startPoint, Point endPoint) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return PathCalculatorClient.getInstance().requestSolution(this);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
              /*  ()-> {
            try {
                Thread.sleep(500);
            } catch (Exception ignored) {}
            Point mapStart = toMapPoint(startPoint), mapEnd = toMapPoint(endPoint);
            List<Point> response = new ArrayList<>();
            Point pointToAdd = new Point(mapStart.x, mapStart.y);
            while (pointToAdd.x != mapEnd.x) {
                response.add(pointToAdd);
                pointToAdd = new Point(pointToAdd.x + Double.compare(mapEnd.x, pointToAdd.x), pointToAdd.y);
            }
            while (pointToAdd.y != mapEnd.y) {
                response.add(pointToAdd);
                pointToAdd = new Point(pointToAdd.x, pointToAdd.y + Double.compare(mapEnd.y, pointToAdd.y));
            }
            return response;
        } */
        );
    }

    public Point toMapPoint(Point geoPoint) {
        double x = (geoPoint.x - getStartPosition().x) / getSquareSize();
        double y = (geoPoint.y - getStartPosition().y) / getSquareSize();
        return new Point((int)x, (int)y);
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
        return squareSize.orElseThrow(()->new NoSuchElementException("No value present"));
    }

    public Point getStartPosition() {
        return startPosition.orElseThrow(()->new NoSuchElementException("No value present"));
    }

    public Point getCurrentPlaneLocation() {
        return currentPosition.orElseThrow(()->new NoSuchElementException("No value present"));
    }

    public ColorAndHeight[][] getColorMap() {
        return map.orElseThrow(()->new NoSuchElementException("No value present"));
    }

    public void setEndPosition(Point end) {
        this.endPosition = Optional.of(end);
    }

    public Point getEndPosition() {
        return endPosition.orElseThrow(()->new NoSuchElementException("No value present"));
    }

    public void setCurrentLocation(Point currentLocation) {
        this.currentPosition = Optional.of(new Point(currentLocation.x, currentLocation.y));
    }

    public void setSquareSize(double squareSize) {
        this.squareSize = Optional.of(squareSize);
    }
}
