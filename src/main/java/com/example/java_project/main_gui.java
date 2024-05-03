package com.example.java_project;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.util.Optional;


public class main_gui extends Application {
    private Stage primaryStage;
    private Label filePathLabel;
    @Override
    public void start(Stage primaryStage) {
        // Setting the stage to be maximized
        primaryStage.setMaximized(true);

        // Creating the main layout
        VBox root = new VBox(40);
//        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #bde0fe;-fx-display:flex");
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(400, 400);

        VBox leftBox = new VBox(20);
        leftBox.setStyle("-fx-background-color: #023047;");
        leftBox.setMaxWidth(400);
        leftBox.setMinHeight(500);
        leftBox.setAlignment(Pos.CENTER);

        Image image = new Image("https://fluentdigital.co/wp-content/uploads/2023/01/cybersecurity.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);


        leftBox.getChildren().add(imageView);


        // Creating the login box
        VBox loginBox = new VBox(20);
        loginBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 0px;");
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
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);

        });

        Button signupButton = new Button("SignUp");
        signupButton.setOnAction(e ->{
            createUser();
        });
        // Adding the fields to the login box
        loginBox.getChildren().addAll(titleLabel, usernameField, passwordField,loginButton,signupButton);



        // Adding the login button to the main layout
//        root.getChildren().addAll(loginBox,leftBox);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(leftBox, loginBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setHgrow(leftBox, Priority.ALWAYS);
        hBox.setHgrow(loginBox, Priority.ALWAYS);

        root.getChildren().add(hBox);
        // Setting up the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to create the success scene
    private Scene createSuccessScene() {
        VBox Main = new VBox();

        HBox navLayout = new HBox();
        navLayout.setStyle("-fx-background-color: #023047");
        navLayout.setMinHeight(120);
        navLayout.setAlignment(Pos.CENTER_LEFT);
        navLayout.setSpacing(10); // Adds spacing between elements

        Label titleLabel = new Label("WEB SERVER");
        titleLabel.setFont(Font.font("Poppins", 40));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        Image image = new Image("https://cdn-icons-png.flaticon.com/512/10479/10479731.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        HBox navRight = new HBox();
        navRight.setAlignment(Pos.CENTER_RIGHT);
        navRight.minWidth(900);
        navRight.setSpacing(50);
        navRight.setPadding(new Insets(0,0,0,750));

        Button home = new Button("HOME");
        Button about = new Button("ABOUT");
        Button contact = new Button("CONTACT");
        Button profile = new Button("Profile");

        // Create and configure the pop-up dialog
        Dialog<String> profileDialog = new Dialog<String>();
        profileDialog.setTitle("Profile Information");
        ButtonType type = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        ButtonType type1 = new ButtonType("Edit",ButtonData.NEXT_FORWARD);
        profileDialog.getDialogPane().getButtonTypes().addAll(type,type1);

        Dialog<String> editDialog = new Dialog<String>();
        editDialog.setTitle("Edit Profile");
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        ButtonType save = new ButtonType("Edit",ButtonData.NEXT_FORWARD);
        editDialog.getDialogPane().getButtonTypes().addAll(type,type1);

        profile.setOnAction(e -> {
            // Show the pop-up dialog
            profileDialog.setResultConverter(dialogButton -> {
                if (dialogButton == type) {
                    // Handle OK button action
                    return "Hi";
                } else if (dialogButton == type1) {
                    // Handle Next button action
                    editDialog.showAndWait();
                }
                return null;
            });
            Optional<String> result = profileDialog.showAndWait();
            result.ifPresent(System.out::println);
        });



        navRight.getChildren().addAll(home,about,contact,profile);

        navLayout.getChildren().addAll(imageView,titleLabel,navRight);

        VBox outer = new VBox();
        outer.setAlignment(Pos.CENTER);
        outer.setStyle("-fx-background-color: #bde0fe;-fx-display:flex");
        outer.setPadding(new Insets(100));

        HBox inner =  new HBox(20);
        inner.setStyle("-fx-background-color: #023047");
        inner.setPrefSize(1000,500);
        inner.setMaxWidth(1000);
        inner.setAlignment(Pos.CENTER);
        inner.setSpacing(0);


        VBox innerLeft = new VBox(10);
        innerLeft.setPrefSize(500,250);
        innerLeft.setStyle("-fx-background-color: #ffffff;");
        innerLeft.setMinHeight(100);
        innerLeft.setAlignment(Pos.CENTER);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(120));
        layout.setVgap(10);
        layout.setHgap(10);

        // Labels and TextFields for user input
        Label IPLabel = new Label("Server IP:");
        TextField IPField = new TextField();
        Label portLabel = new Label("Server Port:");
        TextField portField = new TextField();

        IPField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}(\\.\\d{0,3}){0,3}")) {
                IPField.setText(oldValue);
            }
        });

        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Add components to layout
        layout.add(IPLabel, 0, 0);
        layout.add(IPField, 1, 0);
        layout.add(portLabel, 0, 1);
        layout.add(portField, 1, 1);

        Button button = new Button("Open File Chooser");

        // Create a label to display the selected file path
        Label filePathLabel = new Label();

        // Set action for the button
        button.setOnAction(e -> {
            // Create a FileChooser
            FileChooser fileChooser = new FileChooser();

            // Set title for FileChooser
            fileChooser.setTitle("Select File");

            // Show open file dialog
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // If a file is selected, display its path in the label
            if (selectedFile != null) {
                filePathLabel.setText("Selected File: " + selectedFile.getAbsolutePath());
            } else {
                filePathLabel.setText("No file selected.");
            }
        });

        Button sendButton = new Button("Send to Server");
        innerLeft.getChildren().addAll(layout,button,filePathLabel,sendButton);

        VBox innerRight = new VBox(10);
        innerRight.setPrefSize(500,500);
        innerRight.setStyle("-fx-background-color: #023047;");
        innerRight.setAlignment(Pos.CENTER);
        innerRight.setMinHeight(100);

        Label welcome = new Label("WELCOME");
        welcome.setStyle("-fx-text-fill: #ffffff;");
        welcome.setFont(Font.font("Poppins", 20));

        String username = "Hevardhan";

        Label displayName = new Label(username);
        displayName.setStyle("-fx-text-fill: #ffffff;");
        displayName.setFont(Font.font("Poppins", 40));

        Image imageInner = new Image("https://cdni.iconscout.com/illustration/premium/thumb/cyber-security-2974902-2477419.png");
        ImageView imageViewInner = new ImageView(imageInner);
        imageViewInner.setFitHeight(300);
        imageViewInner.setPreserveRatio(true);

        innerRight.getChildren().add(imageViewInner);
        inner.getChildren().addAll(innerLeft,innerRight);
        inner.setHgrow(innerLeft, Priority.ALWAYS);
        inner.setHgrow(innerLeft, Priority.ALWAYS);

        outer.getChildren().add(inner);
        Main.getChildren().addAll(navLayout,outer);

        return new Scene(Main);
    }

    private void createUser() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Create User");

        // Layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setVgap(10);
        layout.setHgap(10);

        // Labels and TextFields for user input
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        // Add components to layout
        layout.add(nameLabel, 0, 0);
        layout.add(nameField, 1, 0);
        layout.add(emailLabel, 0, 1);
        layout.add(emailField, 1, 1);

        // Button to submit
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Here you can handle the submission of user data
            // For simplicity, let's just print the data for now
            System.out.println(STR."Name: \{nameField.getText()}");
            System.out.println(STR."Email: \{emailField.getText()}");
            popupStage.close();
        });
        layout.add(submitButton, 1, 2);

        // Scene
        Scene scene = new Scene(layout, 300, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
