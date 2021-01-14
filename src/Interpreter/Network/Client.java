package Interpreter.Network;

import Interpreter.Variables.Fundation.VariableManager;
import Interpreter.Variables.Fundation.VariableProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private static Client instance;

    private final VariableManager variableManager = VariableProvider.getInstance();
    private String hostname;
    private int port;
    private int frequency;
    private CompletableFuture cli;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void start() {
        if (cli != null) {
            cli.cancel(true);
        }
        cli = CompletableFuture.runAsync(() -> {
            try (Socket cli = new Socket()) {
                while (!cli.isConnected()) {
                    try {
                        cli.connect(new InetSocketAddress(hostname, port));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.isRunning.set(true);
                do {
                    try {
                        sendData(cli.getOutputStream());
                        TimeUnit.MILLISECONDS.sleep(1000 / frequency);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (this.isRunning.get());

                cli.getOutputStream().write("bye".getBytes());
                cli.getOutputStream().flush();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.isRunning.set(false);
            }
        });
    }

    public void stop() {
        this.isRunning.set(false);
        try {
            cli.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendData(OutputStream os) throws IOException {
        variableManager.readLock().lock();
        try {
            for (String var : new String[]{"simX", "simY", "simZ"}) {
                if (variableManager.getValue(var) != null) {
                    os.write(("set " + var + " " + variableManager.getValue(var)).getBytes());
                    os.flush();
                }
            }
        } finally {
            variableManager.readLock().unlock();
        }
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
