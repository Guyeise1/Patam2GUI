package Interpreter.Network;

import Interpreter.Commands.util.OPENDATASERVER;
import Interpreter.Variables.Fundation.VariableManager;
import test.MyInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    private static Server instance;
    private int port;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private CompletableFuture ser;

    private Server() {
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start() {
        ser = CompletableFuture.runAsync(() -> {
            isRunning.set(true);
            try (ServerSocket server = new ServerSocket()) {
                server.bind(new InetSocketAddress("127.0.0.1", OPENDATASERVER.port));
                Socket client = server.accept();
                try (InputStream inputStream = client.getInputStream()) {
                    do {
                        try {
                            byte[] buffer = new byte[1024];
                            int len = inputStream.read(buffer);
                            String data = new String(buffer, 0, len);
                            parseData(data);
                            System.out.println("Server got: " + data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } while (isRunning.get());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.isRunning.set(false);
            }
        });
    }

    public void stop() {
        this.isRunning.set(false);
    }

    private void parseData(String data) {
        VariableManager variableManager = MyInterpreter.getInstance().getVariablesFactory();
        variableManager.writeLock().lock();
        try {
            String[] vars = data.split("\r\n", 2)[0].split(",");
            variableManager.setValue("simX", Double.parseDouble(vars[0]));
            variableManager.setValue("simY", Double.parseDouble(vars[1]));
            variableManager.setValue("simZ", Double.parseDouble(vars[2]));
        } finally {
            variableManager.writeLock().unlock();
        }
    }

    public AtomicBoolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(AtomicBoolean isRunning) {
        this.isRunning = isRunning;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
