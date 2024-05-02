package com.example.java_project;

import com.example.java_project.AESEncryptor;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class FileReceiver {

    public static void receiveFile(String serverIp, int serverPort) {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Waiting for connection on port " + serverPort);

            File directory = new File("src/main/java/com/example/java_project/received_files");
            if (!directory.exists()) directory.mkdirs();

            try (Socket socket = serverSocket.accept();
                 InputStream inputStream = socket.getInputStream()) {
                System.out.println("Connection accepted from " + socket.getInetAddress());

                // Initialize ByteArrayOutputStream to hold the credentials
                ByteArrayOutputStream credentialStream = new ByteArrayOutputStream();
                int data;
                // Read bytes until a newline is encountered
                while ((data = inputStream.read()) != -1) {
                    if ((char) data == '\n') break;  // Stop reading at newline, which signifies end of credentials
                    credentialStream.write(data);
                }
                String credentials = credentialStream.toString("UTF-8");
                // Check if the credentials are valid
                if (!credentials.equals("123")) {  // Assuming "123" is your placeholder for valid credentials
                    System.out.println("Invalid credentials");
                    return;  // End the function if credentials are invalid
                }
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write('1');

                System.out.println("Valid credentials received.");

                System.out.println("Connection Accepted , receiving files");
                // Receive file name
                ByteArrayOutputStream fileNameStream = new ByteArrayOutputStream();
                System.out.println("Initialized filenamestream");
                int byteRead;
                System.out.println("Initialized byteread");

                if (inputStream.available() > 0) {
                    while ((byteRead = inputStream.read()) != -1) {
                        System.out.print("Running while loop");
                        if ((char) byteRead == '\n') break;
                        fileNameStream.write(byteRead);
                    }
                } else {
                    System.out.println("No data available to read");
                }


                while ((byteRead = inputStream.read()) != -1) {
                    System.out.print("Running while loop");
                    if ((char) byteRead == '\n') break;
                    fileNameStream.write(byteRead);
                }
                String fileName = fileNameStream.toString("UTF-8");
                System.out.println("File Name : " + fileName);

                // Receive encrypted file data
                ByteArrayOutputStream encryptedDataStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    encryptedDataStream.write(buffer, 0, bytesRead);
                }

                String key = new String(Files.readAllBytes(Paths.get("src/main/java/com/example/java_project/key.txt")));
                AESEncryptor aesEncryptor = new AESEncryptor(key);
                byte[] decryptedData = aesEncryptor.decryptData(encryptedDataStream.toByteArray());

                // Write received file data to file
                try (FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, fileName))) {
                    fileOutputStream.write(decryptedData);
                }
                System.out.println("File received and decrypted successfully.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //
        //
        //
        // Testing H2 databse with JDBC [IGNORE]
        try {
            Class.forName("org.h2.Driver");
            try (Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "admin", "admin");
                 Statement statement = connection.createStatement()) {
                ResultSet execute = statement.executeQuery("SELECT 3*4");
                if (execute.next()) {
                    System.out.println(execute.getString(1));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //
        //
        //
        // Main init
        String serverIp = "127.0.0.1";  // For local connection for testing purposes or LAN
        int serverPort = 8080;
        receiveFile(serverIp, serverPort);
    }
}
