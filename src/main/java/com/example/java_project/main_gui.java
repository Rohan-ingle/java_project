package com.example.java_project;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class main_gui extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Setting the stage to be maximized
        primaryStage.setMaximized(true);

        // Creating the main layout
        VBox root = new VBox(20);
//        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #bde0fe;");
        root.setAlignment(Pos.CENTER);

        // Creating the login box
        VBox loginBox = new VBox(20);
        loginBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px;-fx-background-radius: 50px;");
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setMaxWidth(400);
        loginBox.setMinHeight(500);

        // Adding the LOGIN title
        Label titleLabel = new Label("LOGIN");
        titleLabel.setFont(Font.font("Poppins", 40));
        titleLabel.setStyle("-fx-padding:0px;-fx-margin:40px");

        // Creating the username and password fields
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);
        passwordField.setMaxWidth(250);
        passwordField.setPromptText("Password");
        // Creating the login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            // Check login credentials here (for simplicity, we'll just assume login is successful)
            // For a real application, you would perform authentication here
            primaryStage.setScene(createSuccessScene());
        });

        // Adding the fields to the login box
        loginBox.getChildren().addAll(titleLabel, usernameField, passwordField,loginButton);

        Button createUserButton = new Button("Sign UP");
        createUserButton.setOnAction(e ->{
            primaryStage.setScene(createUser());
        });

        // Adding the login button to the main layout
        root.getChildren().addAll(loginBox,createUserButton);

        // Setting up the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to create the success scene
    private Scene createSuccessScene() {
        VBox successLayout = new VBox(20);
        successLayout.setPadding(new Insets(50));
        successLayout.setStyle("-fx-background-color: #f0f0f0;");
        successLayout.setAlignment(Pos.CENTER);

        Label successLabel = new Label("Login Successful");
        successLabel.setFont(Font.font("Poppins", 20));

        successLayout.getChildren().add(successLabel);

        return new Scene(successLayout,400,500);
    }

    private Scene createUser() {
        VBox successLayout = new VBox(40);
        successLayout.setPadding(new Insets(50));
        successLayout.setStyle("-fx-background-color: #f0f0f0;");
        successLayout.setAlignment(Pos.CENTER);

        Label successLabel = new Label("User Created Successfully");
        successLabel.setFont(Font.font("Poppins", 20));

        successLayout.getChildren().add(successLabel);

        return new Scene(successLayout,400,500);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
