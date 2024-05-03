package com.example.java_project;
import javafx.application.Application;
import javafx.event.Event;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import javafx.stage.WindowEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;


public class main_gui extends Application {
    private Stage primaryStage;
    private Label filePathLabel;

    private String serverIP;
    private String serverPort;
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
            try {
                serverPort.equals(null);

                primaryStage.setScene(createSuccessScene());
                primaryStage.setMaximized(false);
                primaryStage.setMaximized(true);
            } catch (NullPointerException f) {
                // Handle NullPointerException
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Server IP or Port is invalid. Restart the Application");
                alert.showAndWait();
            }



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

        // Connect to Server Dialogue Box
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Connect to Server");

        // Set the button types.
        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        // Create the IP and Port labels and fields.
        Label infoLabel = new Label("Enter Valid IP and Port");
        Label ipLabel = new Label("IP:");
        Label portLabel = new Label("Port:");
        TextField ipTextField = new TextField();
        TextField portTextField = new TextField();

        // Add IP and Port fields to the dialog layout.
        GridPane grid = new GridPane();
        grid.add(ipLabel, 1, 1);
        grid.add(ipTextField, 2, 1);
        grid.add(portLabel, 1, 2);
        grid.add(portTextField, 2, 2);
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
                serverIP = ipTextField.getText();
                serverPort = portTextField.getText();
                return ipTextField.getText() + ":" + portTextField.getText();
            }
            return null;
        });

        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setOnAction(e -> {
            dialog.close();
            primaryStage.close(); // Close primary stage if cancel button is pressed
        });

        dialog.showAndWait().ifPresent(result -> {
            System.out.println("Connect to: " + result);
            // You can perform connection logic here with the entered IP and Por
        });
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

        Button ipInfoButton = new Button("IP INFO");
        Button deleteButton = new Button("DELETE");
        Button helpButton = new Button("HELP");
        Button profile = new Button("PROFILE");

        // Create and configure the pop-up dialog
        Dialog<String> profileDialog = new Dialog<String>();
        profileDialog.setTitle("Profile Information");
        ButtonType type = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        ButtonType type1 = new ButtonType("Edit",ButtonData.NEXT_FORWARD);
        profileDialog.getDialogPane().getButtonTypes().addAll(type,type1);

        GridPane grid = new GridPane();

        grid.setHgap(10);

        grid.setVgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(new Label("Hevardhan"), 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(new Label("fuckoff@gmail.com"), 1, 1);

        // Add the labels to the dialog pane
        profileDialog.getDialogPane().setContent(grid);

        Dialog<String> editDialog = new Dialog<>();
        editDialog.setTitle("Edit Profile");

        // Create text fields for username and password
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        usernameField.setText("hevardhan");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        editDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create a GridPane to layout the dialog content
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        // Add the text fields to the GridPane
        gridPane.add(new Label("Username:"), 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(new Label("Password:"), 0, 1);
        gridPane.add(passwordField, 1, 1);

        // Enable/Disable save button depending on whether a username and password is entered
        Node saveButton = editDialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || passwordField.getText().isEmpty());
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || usernameField.getText().isEmpty());
        });

        // Set the dialog content
        editDialog.getDialogPane().setContent(gridPane);


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

        ipInfoButton.setOnAction(e ->{
            // Handle NullPointerException
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("IP Information");
            alert.setHeaderText(null);
            alert.setContentText("Your IP Address is ");
            alert.showAndWait();
        });

        deleteButton.setOnAction(e -> {
            // Show confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete the account?");
            confirmationAlert.setContentText("If you delete the account, it cannot be recovered.");

            // Show confirmation dialog and wait for user's response
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // If user confirms deletion, show username as label and ask for password
                    Dialog<String> passwordDialog = new Dialog<>();
                    passwordDialog.setTitle("Confirm Deletion");

                    // Create label to display username
                    Label usernameLabel = new Label("Username:");
                    Label usernameContent = new Label("hevardhan");

                    // Create password field for input
                    PasswordField passwordField0 = new PasswordField();
                    passwordField0.setPromptText("Enter Password");

                    // Set the button types
                    ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                    passwordDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                    // Create a GridPane to layout the dialog content
                    GridPane gridPane0 = new GridPane();
                    gridPane0.setHgap(10);
                    gridPane0.setVgap(10);
                    gridPane0.setPadding(new Insets(20));

                    // Add the components to the GridPane
                    gridPane0.add(usernameLabel, 0, 0);
                    gridPane0.add(usernameContent,1,0);
                    gridPane0.add(new Label("Password:"), 0, 1);
                    gridPane0.add(passwordField0, 1, 1);

                    // Enable/Disable confirm button depending on whether password is entered
                    Node confirmButton = passwordDialog.getDialogPane().lookupButton(confirmButtonType);
                    confirmButton.setDisable(true);

                    passwordField0.textProperty().addListener((observable, oldValue, newValue) -> {
                        confirmButton.setDisable(newValue.trim().isEmpty());
                    });

                    // Set the dialog content
                    passwordDialog.getDialogPane().setContent(gridPane0);

                    // Convert the result to a password when the confirm button is clicked
                    passwordDialog.setResultConverter(dialogButton -> {
                        if (dialogButton == confirmButtonType) {
                            return passwordField0.getText();
                        }
                        return null;
                    });

                    // Show the password dialog and wait for the user's response
                    passwordDialog.showAndWait().ifPresent(password -> {
                        Alert SuccessAlert = new Alert(AlertType.INFORMATION);
                        SuccessAlert.setTitle("Info");
                        SuccessAlert.setHeaderText("Account Deleted Successfully !");
                        SuccessAlert.showAndWait();
                        // Perform deletion logic here
                    });
                }
            });
        });


        navRight.getChildren().addAll(ipInfoButton,deleteButton,helpButton,profile);

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
        innerLeft.setSpacing(80);

        VBox leftTop = new VBox();
        leftTop.setAlignment(Pos.CENTER);
        Label sendFileTitle = new Label("File Sender");
        sendFileTitle.setFont(Font.font("Poppins", 40));
        Button openFilebutton = new Button("Open File Chooser");
        leftTop.setSpacing(10);

        // Create a label to display the selected file path
        Label filePathLabel = new Label();

        // Set action for the button
        openFilebutton.setOnAction(e -> {
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
        leftTop.getChildren().addAll(sendFileTitle,openFilebutton,filePathLabel,sendButton);

        VBox leftDown = new VBox();
        leftDown.setAlignment(Pos.CENTER);
        leftDown.setSpacing(10);

        Label receiveFileTitle = new Label("File Receiver");
        receiveFileTitle.setFont(Font.font("Poppins", 40));

        // Create a label to display the selected file path
        Label filePathLabel0 = new Label("Selected File: Option 1");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(
                "Option 1",
                "Option 2",
                "Option 3"
        ));
        comboBox.setOnAction(e -> {
            String selectedOption = comboBox.getValue();
            filePathLabel0.setText("Selected File: " + selectedOption);
        });

        // Set a default value for the dropdown
        comboBox.setValue("Option 1");

        Button receiveButton = new Button("Retrieve from Server");
        leftDown.getChildren().addAll(receiveFileTitle,comboBox,filePathLabel0,receiveButton);

        innerLeft.getChildren().addAll(leftTop,leftDown);

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
            System.out.println(STR."ID: \{nameField.getText()}");
            System.out.println(STR."PASSWORD: \{emailField.getText()}");
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
