package pathCalculator;

import sample.Map.MapController;
import sample.Map.MapModel;
import sample.StaticClasses.Point;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PathCalculatorClient {

    private static PathCalculatorClient instance;

    public static final InetAddress DEFAULT_SERVER_HOST = InetAddress.getLoopbackAddress();
    public static final int DEFAULT_PORT = 15000;
    public static final String SEPERATOR = "~";


    private boolean isConnected;
    private InputStream inputStream;
    private OutputStream outputStream;

    // Do not allow external instances of Path Calculator
    private PathCalculatorClient() {
        isConnected = false;
        inputStream = null;
        outputStream = null;
    }

    public static PathCalculatorClient getInstance() {
        if (instance == null)
            instance = new PathCalculatorClient();
        return instance;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        isConnected = true;
    }

    public List<Point> requestSolution(MapModel model) throws IOException {
        byte[] request = buildRequestByteArray(model);
        this.outputStream.write(request);
        byte[] buffer = new byte[1024000];
        int read = inputStream.read(buffer);
        String msg = new String(buffer, 0, read);
        return responseAnswers(msg);
    }

    private byte[] buildRequestByteArray(MapModel model) {
        Point startPoint = model.toMapPoint(model.getStartPosition()),
                endPoint = model.toMapPoint(model.getEndPosition());
        String answer = "";
        String startPointString = displayDouble(startPoint.x) + SEPERATOR + displayDouble(startPoint.y) + "\n",
                endPointString = displayDouble(endPoint.x) + SEPERATOR + displayDouble(endPoint.y) + "\n";
        answer += startPointString + endPointString;
        // Up to 3 digits for height + \n at the end of line
        StringBuffer buffer = new StringBuffer(model.getColorMap().length * model.getColorMap()[0].length * 4);
        for (int x = 0; x < model.getColorMap().length; x++) {
            for (int y = 0; y < model.getColorMap()[x].length; y++) {
                buffer.append((int)model.getColorMap()[x][y].height);
                buffer.append(",");
            }
            buffer.append("\n");
        }
        answer += buffer.toString();
        return answer.getBytes();
    }

    private List<Point> responseAnswers(String response) {
        String [] points = response.split(",");
        List<Point> answer = new ArrayList<>(points.length);
        for (int x = 0; x < points.length; x++) {
            String[] coords = points[x].split(SEPERATOR, 3);
            answer.add(new Point(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
        }
        return answer;
    }

    private String displayDouble(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        inputStream = null;
        outputStream = null;
    }



}
