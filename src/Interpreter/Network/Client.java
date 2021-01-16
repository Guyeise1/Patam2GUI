package Interpreter.Network;

import Interpreter.Commands.Fundation.VariablesFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private static Client instance;

    private String hostname;
    private int port;
    private int frequency;
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    private Client(){}

    public void start() {
        CompletableFuture.runAsync( () -> {
            try (Socket cli = new Socket()) {
                cli.connect(new InetSocketAddress(hostname, port));
                this.isRunning.set(true);
                while (this.isRunning.get()) {
                    sendData(cli.getOutputStream());
                    TimeUnit.MILLISECONDS.sleep(1000 / frequency);
                }

                cli.getOutputStream().write("bye".getBytes());
                cli.getOutputStream().flush();
                cli.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.isRunning.set(false);
            }
        });
    }

    public void stop() {
        this.isRunning.set(false);
    }

    private void sendData(OutputStream os) throws IOException {
        for (String var : new String[]{"simX", "simY","simZ"} ) {
            try {
                os.write(("set " + var + " " + VariablesFactory.getInstance().getVariable(var).value.toString()).getBytes());
            } catch (RuntimeException e) {
                // System.out.println("did not found variable " + var + " ignoring...");
            }
        }
        os.flush();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
