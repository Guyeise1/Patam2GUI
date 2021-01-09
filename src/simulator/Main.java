package simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {


    @SuppressWarnings("all")
    private static CompletableFuture<Void> read() {
        return CompletableFuture.runAsync(() -> {
            try {
                while (true) {
                    System.out.print(NetworkCommands.getInstance().read() + " ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private static CompletableFuture<Void> write() {
        return CompletableFuture.runAsync(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String cmd;
                while(true) {
                    cmd = reader.readLine();
                    if(cmd.equals("exit")) {
                        return;
                    } else {
                        NetworkCommands.getInstance().write(cmd);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        NetworkCommands.getInstance().connect("Guy-VM", 5402);
        CompletableFuture<Void> r = read();
        CompletableFuture<Void> w = write();

        w.get();
        r.cancel(true);

    }
}
