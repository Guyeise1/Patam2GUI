package simulator;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkCommands implements Closeable {
    private static NetworkCommands instance = null;

    public static NetworkCommands getInstance() {
        if (instance == null) {
            instance = new NetworkCommands();
        }
        return instance;
    }

    private NetworkCommands() {
    }


    private Socket client;

    public void connect(String hostname, int port) throws IOException {
        if (client == null) {
            client = new Socket();
            client.connect(new InetSocketAddress(hostname, port), 5000);
        }
    }

    public String read() throws IOException {
        if (client == null) {
            throw new NullPointerException("execute connect before this command.");
        }

        InputStream in = client.getInputStream();
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        return new String(buffer,0, read);
    }

    public void write(String msg) {
        if (client == null) {
            throw new NullPointerException("execute connect before this command.");
        }

        try {
            OutputStream ops = client.getOutputStream();
            new PrintWriter(ops, true).println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            try {
                client.close();
            } catch (IOException ignored) {
            }
        }
    }
}
