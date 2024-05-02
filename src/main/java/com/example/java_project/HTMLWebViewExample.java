//package com.example.java_project;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
//import javafx.stage.Stage;
//
//public class HTMLWebViewExample extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Creating a WebView
//        WebView webView = new WebView();
//
//        // Loading HTML content into the WebView
//        String htmlContent = "<html><body><h1>Hello, HTML in JavaFX!</h1></body></html>";
//        webView.getEngine().loadContent(htmlContent);
//
//        // Creating a scene and adding the WebView to it
//        Scene scene = new Scene(webView, 600, 400);
//
//        // Setting the stage title and scene
//        primaryStage.setTitle("HTML in JavaFX");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
