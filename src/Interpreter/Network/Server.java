package Interpreter.Network;

import Interpreter.Commands.Fundation.Variables;
import Interpreter.Commands.util.OPENDATASERVER;
import test.MyInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    private int port;
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private Server() {}

    private static Server instance;
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start() {
        CompletableFuture.runAsync(() -> {
            isRunning.set(true);
            try (ServerSocket server = new ServerSocket()) {
                server.bind(new InetSocketAddress("127.0.0.1", OPENDATASERVER.port));
                Socket client = server.accept();
                try (InputStream inputStream = client.getInputStream()) {
                    while (isRunning.get()) {
                        try {
                            byte[] buffer = new byte[1024];
                            int len = inputStream.read(buffer);
                            String data = new String(buffer, 0, len);
                            parseData(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    server.close();
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
        String[] vars = data.split(",");
        Variables variablesFactory = MyInterpreter.getInstance().getVariablesFactory();

        if(!variablesFactory.containsVariable("simX")) {
            variablesFactory.createVariable("simX");
        }
        double simX =  Double.parseDouble(vars[0]);
        variablesFactory.assignValue("simX", Double.parseDouble(vars[0]));
        System.out.println("simX set to: " +simX);

        if(!variablesFactory.containsVariable("simY")) {
            variablesFactory.createVariable("simY");
        }
        double simY =  Double.parseDouble(vars[1]);
        variablesFactory.assignValue("simY", Double.parseDouble(vars[1]));
        System.out.println("simY set to: " +simY);

        if(!variablesFactory.containsVariable("simZ")) {
            variablesFactory.createVariable("simZ");
        }
        double simZ =  Double.parseDouble(vars[2]);
        variablesFactory.assignValue("simZ", Double.parseDouble(vars[2]));
        System.out.println("simZ set to: " +simZ);
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
