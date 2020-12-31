package sample.Map;

import sample.StaticClasses.ItemInMap;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapModel {

    public ItemInMap[][] createMapFromFile(String fileName) throws IOException, VerifyError {
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
        verifyMatrix(heigths);
        int d_1 = heigths.size();
        int d_2 = heigths.get(0).size();
        ItemInMap[][] matrix = new ItemInMap[d_1][d_2];
        Function<Integer, String> heightToColor = buildColorFromHeightFunction(max_height);
        for (int x = 0; x < d_1; x++) {
            matrix[x] = new ItemInMap[d_2];
            for (int y = 0; y < d_2; y++) {
                int height = heigths.get(x).get(y);
                matrix[x][y] = new ItemInMap(heightToColor.apply(height), height);
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

    private String stringOfHex(int n) {
        return String.format("%02X", n);
    }

    private Function<Integer, String> buildColorFromHeightFunction(int maxHeight)
    {
//        final int BLUE_VALUE = 51;
//        final int GREEN_MIN_VALUE = 0, GREEN_MAX_VALUE = 255;
//        final int RED_MIN_VALUE = 0, RED_MAX_VALUE = 255;
//
//        if (minHeight == maxHeight) {
//            String r = Integer.toHexString(RED_MAX_VALUE),
//                    g = Integer.toHexString(GREEN_MAX_VALUE),
//                    b = Integer.toHexString(BLUE_VALUE);
//            return height -> { return "#" + r + g + b; };
//        } else {
////            int deltaY = (RED_MAX_VALUE - RED_MIN_VALUE) + (GREEN_MAX_VALUE - GREEN_MIN_VALUE);
////            int deltaX = maxHeight - minHeight;
//
////            double slope = 1.0 * deltaY / deltaX;
//            // x is the height of the plane, result is color of background
//            return x -> {
//
//                myColor = new Color(2.0f * x, 2.0f * (1 - x), 0);
//            }
//        }

        return height ->  {
            final int MAX_COLOR = 255;
            int color = MAX_COLOR * height  / maxHeight;
            String r = stringOfHex(color),
                    g = stringOfHex(MAX_COLOR - color),
                    b = stringOfHex(0);
            return "#" + r + g + b;
        };
    }


}
