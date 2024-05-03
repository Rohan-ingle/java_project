package com.example.java_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class Main extends Application {

    private Stage primaryStage;
    private Label filePathLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("JavaFX File Chooser");

        // Sign-in Page
        VBox signInPage = new VBox(10);
        signInPage.setPadding(new Insets(10));
        signInPage.getChildren().add(new Label("Sign In Page"));

        // Main Application Page
        VBox mainPage = new VBox(10);
        mainPage.setPadding(new Insets(10));
        Button openFileChooserButton = new Button("Open File Chooser");
        filePathLabel = new Label();
        openFileChooserButton.setOnAction(e -> openFileChooser());
        mainPage.getChildren().addAll(openFileChooserButton, filePathLabel);

        // Initially show the sign-in page
        Scene scene = new Scene(signInPage, 300, 200);

        // Change to the main page after signing in
        signInPage.setOnMouseClicked(event -> primaryStage.setScene(new Scene(mainPage, 300, 200)));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            filePathLabel.setText("Selected File: " + selectedFile.getAbsolutePath());
        } else {
            filePathLabel.setText("No file selected");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
