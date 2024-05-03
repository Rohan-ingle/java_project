package com.example.java_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome");

        Label welcomeLabel = new Label("Welcome to the application!");

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Connect to Server");

        // Set the button types.
        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        // Create the IP and Port labels and fields.
        Label ipLabel = new Label("IP:");
        Label portLabel = new Label("Port:");
        TextField ipTextField = new TextField();
        TextField portTextField = new TextField();

        // Add IP and Port fields to the dialog layout.
        GridPane grid = new GridPane();
        grid.add(ipLabel, 0, 0);
        grid.add(ipTextField, 1, 0);
        grid.add(portLabel, 0, 1);
        grid.add(portTextField, 1, 1);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Enable/disable connect button based on input validity
        Button connectButton = (Button) dialog.getDialogPane().lookupButton(connectButtonType);
        connectButton.setDisable(true);

        // Validation for IP and Port fields
        Pattern ipPattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Pattern portPattern = Pattern.compile("^\\d{1,5}$");

        ipTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            connectButton.setDisable(!ipPattern.matcher(newValue).matches() || !portPattern.matcher(portTextField.getText()).matches());
        });

        portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            connectButton.setDisable(!portPattern.matcher(newValue).matches() || !ipPattern.matcher(ipTextField.getText()).matches());
        });

        // Add grid to dialog pane
        dialog.getDialogPane().setContent(grid);

        // Convert the result to IP:Port when the connect button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                return ipTextField.getText() + ":" + portTextField.getText();
            }
            return null;
        });

        // Show the dialog
        dialog.showAndWait().ifPresent(result -> {
            System.out.println("Connect to: " + result);
            // You can perform connection logic here with the entered IP and Port
        });


    }

    public static void main(String[] args) {
        launch(args);
    }
}
