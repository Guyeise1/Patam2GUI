package simulator;

import java.io.IOException;
import java.util.Optional;

public class Parameters {

    public static Optional<Double> getDoubleValue(SimulatorParam param) {
        try {
            NetworkCommands.getInstance().write("data");
            NetworkCommands.getInstance().write("get " + param.path);

            String value = NetworkCommands.getInstance().read();
            return Optional.of(Double.parseDouble(value));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static void setDoubleValue(SimulatorParam param, double value) {
        NetworkCommands.getInstance().write("data");
        NetworkCommands.getInstance().write("setd " + param.path + " " + value);
    }


    public static Optional<Double> getDoubleValue(String param) {
        try {
            NetworkCommands.getInstance().write("data");
            NetworkCommands.getInstance().write("get " + param);

            String value = NetworkCommands.getInstance().read();
            return Optional.of(Double.parseDouble(value));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static void setDoubleValue(String param, double value) {
        NetworkCommands.getInstance().write("data");
        NetworkCommands.getInstance().write("setd " + param + " " + value);
    }

    public enum SimulatorParam {
        LATITUDE_DEG("/position/latitude-deg"), // y
        ALTITUDE_FT("/position/altitude-ft"),
        LONGITUDE_DEG("/position/longitude-deg"); // x

        private final String path;


        public String getPath() {
            return path;
        }

        SimulatorParam(String s) {
            this.path = s;
        }

    }

}

