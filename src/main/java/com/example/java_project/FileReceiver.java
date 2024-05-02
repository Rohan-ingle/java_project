package com.example.java_project;
import com.example.java_project.saltHash;
import com.example.java_project.AESEncryptor;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;

import com.example.java_project.Database;

public class FileReceiver {

    public static void receiveFile(String serverIp, int serverPort, Database database) {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Waiting for connection on port " + serverPort);

            File directory = new File("src/main/java/com/example/java_project/received_files");
            if (!directory.exists()) directory.mkdirs();

            try (Socket socket = serverSocket.accept();
                 InputStream inputStream = socket.getInputStream()) {
                System.out.println("Connection accepted from " + socket.getInetAddress());

                // Initialize ByteArrayOutputStream to hold the credentials
                ByteArrayOutputStream credentialStream = new ByteArrayOutputStream();
                ByteArrayOutputStream saltStream = new ByteArrayOutputStream();

                int data;
                // Read bytes until a newline is encountered
                while ((data = inputStream.read()) != -1) {
                    if ((char) data == '\n') {
                        // Stop reading at newline, which signifies end of credentials
                        break;
                    }
                    credentialStream.write(data);
                }

                // Extract username, password hash, and salt from the credentialStream
                String credentials = credentialStream.toString("UTF-8");
                String[] parts = credentials.split(";"); //";" is the delimiter
                String username;
                String passwordHash;
                String streamedPassword;
                String saltString;
                if (parts.length == 3) {
                    username = parts[0];
                    streamedPassword = parts[1];
                    saltString = parts[2];

                    // Use the extracted values as needed
                    System.out.println("Username: " + username);
                    System.out.println("Password to Hash: " + streamedPassword);
                    System.out.println("Salt: " + saltString);
                } else {
                    System.out.println("Invalid credentials format.");
                    return;
                }

                String[] saltParts = saltString.substring(1, saltString.length() - 1).split(", ");
                byte[] salt = new byte[saltParts.length];
                for (int i = 0; i < saltParts.length; i++) {
                    // Remove any non-numeric characters, such as "]" from the end of the string
                    saltParts[i] = saltParts[i].replaceAll("[^\\d-]", "");
                    salt[i] = Byte.parseByte(saltParts[i]);
                }



                String passwordDb = null;

                if(database.userExists(username)) {
                    passwordDb = database.hashedPassword(username);
                    System.out.println("User found");
                }
                else{
                    System.out.println("User not found.");
                    return;
                }

                passwordHash = Arrays.toString(saltHash.hash(streamedPassword.toCharArray(), salt));

                // Check if the credentials are valid
                if (!passwordDb.equals(passwordHash)) {
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
                    System.out.println("No data available to stream or finished streaming name buffer");
                }


                while ((byteRead = inputStream.read()) != -1) {
                    System.out.print("Writing name buffer\n");
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

        Database database = new Database();
        database.establishConnection();

        // Main initialization
        String serverIp = "127.0.0.1";  // For local connection for testing purposes or LAN
        int serverPort = 8080;
        receiveFile(serverIp, serverPort, database);

        // Close the database connection
        database.closeConnection();
    }
}
