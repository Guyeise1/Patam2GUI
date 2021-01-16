package sample.Helpers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.*;
import javafx.util.Pair;

import java.util.Optional;

public class AlertHelper {

    public static void displayAlert(Window owner, Alert.AlertType type, String title, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.initOwner(owner);
        alert.show();
    }

    public static Dialog<Pair<String, Integer>> displayHostAndPort(Window owner, Optional<String> defaultHostname, Optional<Integer> defaultPort) {
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("get ip and port");
        dialog.initOwner(owner);

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField ip = new TextField();
        ip.setPromptText("IP");
        TextField port = new TextField();
        port.setPromptText("Port");
        if (defaultHostname.isPresent()) {
            ip.setText(defaultHostname.get());
        }
        if (defaultPort.isPresent()) {
            port.setText(String.valueOf(defaultPort.get()));
        }
        gridPane.add(new Label("IP:"), 0, 0);
        gridPane.add(ip, 1, 0);
        gridPane.add(new Label("Port:"), 2, 0);
        gridPane.add(port, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        ip.requestFocus();

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(ip.getText(), Integer.parseInt(port.getText()));
            }
            return null;
        });

        return dialog;
    }
    public static Dialog<String> displayFileDialog(Window owner, Optional<String> defaultFileName) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("get file name");
        dialog.initOwner(owner);

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField fileName = new TextField();
        fileName.setPromptText("file name");
        if (defaultFileName.isPresent()) {
            fileName.setText(defaultFileName.get());
        }
        gridPane.add(new Label("Map File Name:"), 0, 0);
        gridPane.add(fileName, 1, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        fileName.requestFocus();

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return fileName.getText();
            }
            return null;
        });

        return dialog;
    }

}
