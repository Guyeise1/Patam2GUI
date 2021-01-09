package pathCalculator;

import java.awt.Point;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;


class Main {

    private static List<Point> findPath(int[][] map, Point source, Point dest) {
        Map<Point, Double> dist = new HashMap<>();
        Map<Point, Point> prev = new HashMap<>();
        List<Point> Q = new ArrayList<>();
        dist.put(source, 0.0);
        prev.put(source, null);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Point p = new Point(i, j);
                if (!p.equals(source)) {
                    dist.put(p, Double.POSITIVE_INFINITY);
                    prev.put(p, null);
                }
                Q.add(p);
            }
        }

        while (!Q.isEmpty()) {
            Q.sort(Comparator.comparingDouble(dist::get));
            Point u = Q.get(0);
            Q.remove(0);
            for (Point v : getNeighbors(map, u)) {
                double alt = dist.get(u) + map[v.x][v.y];
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                }
            }
        }

        List<Point> ret = new ArrayList<>();
        Point curr = dest;
        while (curr != null) {
            ret.add(curr);
            curr = prev.get(curr);
        }

        Collections.reverse(ret);
        return ret;
    }

    private static Set<Point> getNeighbors(int[][] map, Point p) {
        Point[] neighbors = {
                new Point(p.x, p.y - 1),
                new Point(p.x, p.y + 1),
                new Point(p.x - 1, p.y),
                new Point(p.x + 1, p.y)
        };

        Set<Point> ret = new HashSet<>();
        for (Point x : neighbors) {
            if (isValid(map, x)) {
                ret.add(x);
            }
        }
        return ret;

    }

    private static boolean isValid(int[][] map, Point p) {
        try {
            int x = map[p.x][p.y];
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(15000);
        System.out.println("Starting server on 15000");
        while(true) {
            try {
                handleClient(server.accept());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void handleClient(Socket client) throws IOException {
        System.out.println("Handle client: " + client.getInetAddress().getHostName());
        byte[] buffer = new byte[1024000];
        int read = client.getInputStream().read(buffer);
        String msg = new String(buffer, 0, read);
        String[] split = msg.split("\n");
        Point source = deserialize(split[0]);
        System.out.println("Source: " + source);
        Point dest = deserialize(split[1]);
        System.out.println("Destination: " + dest);
        String[] matrix = new String[split.length - 2];
        System.arraycopy(split, 2, matrix, 0, split.length - 2);
        int[][] map = parseMap(matrix);
        String answer =  findPath(map, source, dest).stream().map(Main::serialize).collect(Collectors.joining(","));
        System.out.println("sending: " + answer);
        client.getOutputStream().write(answer.getBytes());
        System.out.println("Closing client socket");
        client.close();
    }

    private static int[][] parseMap(String[] matrix) {
        int[][] map = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            String[] line = matrix[i].split(",");
            map[i] = new int[line.length];
            for (int j = 0; j < line.length; j++) {
                map[i][j] = Integer.parseInt(line[j]);
            }
        }

        return map;
    }

    private static String serialize(Point p) {
        return p.getX() + "~" + p.getY();
    }
    private static Point deserialize(String s) {
        String[] split = s.split("~");
        return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }
}

