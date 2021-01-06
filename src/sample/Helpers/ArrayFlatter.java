package sample.Helpers;

import java.util.ArrayList;
import java.util.List;

public class ArrayFlatter {

    public static final double DECREASE_RATIO = 8;

    public static List<List<Double>> decreaseMatrix(List<List<Double>> original) {
        List<List<List<Double>>> matrixOfCubes = groupMatrixToMatrixCubes(original);
        List<List<Double>>  flattenMatrix = flattenMatrix(matrixOfCubes);
        clearList(original);
        clearList(matrixOfCubes);
        return flattenMatrix;
    }

    private static List<List<List<Double>>> groupMatrixToMatrixCubes(List<List<Double>> original) {

        int countWidthCubes = (int)Math.floor(original.size() / DECREASE_RATIO);
        int countHeightCubes = (int)Math.floor(original.get(0).size() / DECREASE_RATIO);
        int cubeWidth = (int)Math.ceil(original.size() * 1.0 / countWidthCubes);
        int cubeHeight = (int)Math.ceil(original.size() * 1.0 / countHeightCubes);

        List<List<List<Double>>> returnValue = new ArrayList<>(countWidthCubes);
        for (int x = 0; x < countWidthCubes; x++) {
            returnValue.add(new ArrayList<>(cubeHeight));
            for (int y = 0; y < countHeightCubes; y++) {
                returnValue.get(x).add(new ArrayList<>(cubeHeight * cubeWidth));
            }
        }

        for (int x = 0; x < original.size(); x ++){
            for (int y = 0; y < original.get(x).size(); y++) {
                returnValue.get(x % countWidthCubes).get(y % countHeightCubes).add(original.get(x).get(y));
            }
        }

        return returnValue;
    }

    private static double calculateAverage(List<Double> list) {
        return list.stream().mapToDouble(x->x).average().getAsDouble();
    }

    private static List<List<Double>> flattenMatrix(List<List<List<Double>>> cubeMatrix) {
        List<List<Double>> list = new ArrayList<>(cubeMatrix.size());
        for (int x = 0; x < cubeMatrix.size(); x ++) {
            list.add(new ArrayList<>(cubeMatrix.get(x).size()));
            for (int y = 0; y < cubeMatrix.get(x).size(); y++) {
                list.get(x).add(calculateAverage(cubeMatrix.get(x).get(y)));
            }
        }
        return list;
    }

    private static void clearList(List<?> list) {
        for (int i =0; i < list.size(); i++) {
            if (list.get(i) instanceof List) {
                List<?> innerList = (List<?>) list.get(0);
                clearList(innerList);
                innerList = null;
            }
        }
        list.clear();
        list = null;
    }
}
