package com.example.java_project;
import com.example.java_project.saltHash;
import com.example.java_project.AESEncryptor;
import com.example.java_project.FileReceiverFunction;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;

import com.example.java_project.Database;
import javafx.application.Platform;

////public class FileReceiver {
////
////    public static void receiveFile(String serverIp, int serverPort, Database database) {
////        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
////            System.out.println("Waiting for connection on port " + serverPort);
////
////            File directory = new File("src/main/java/com/example/java_project/received_files");
////            if (!directory.exists()) directory.mkdirs();
////
////            try (Socket socket = serverSocket.accept();
////                 InputStream inputStream = socket.getInputStream()) {
////                System.out.println("Connection accepted from " + socket.getInetAddress());
////
////                ByteArrayOutputStream signupPacket = new ByteArrayOutputStream();
////                int firstPacket;
////                //
////                // Read bytes until a newline is encountered
////
////                while ((firstPacket = inputStream.read()) != -1) {
////                    if ((char) firstPacket == '\n') {
////                        break;
////                    }
////                    signupPacket.write(firstPacket);
////                }
////
////                String signUP = signupPacket.toString("UTF-8");
////                System.out.println("Received packet: " + signUP);
////
////                String[] signupPacketParts = signUP.split(";");
////
////                if (signupPacketParts.length > 0 && signupPacketParts[0].equals("CreateUser")) {
////                    System.out.println("Creating User");
////                    database.createDb(signupPacketParts[1], signupPacketParts[2]);
////                }
////
////
////                // Initialize ByteArrayOutputStream to hold the credentials
////                ByteArrayOutputStream credentialStream = new ByteArrayOutputStream();
////                int data;
////                // Read bytes until a newline is encountered
////                while ((data = inputStream.read()) != -1) {
////                    if ((char) data == '\n') {
////                        break;
////                    }
////                    credentialStream.write(data);
////                }
////                //
////                //
////                //
////                //
////                ///////////////////////////////////////////////////////// AUTHENTICATION /////////////////////////////////////////////////////////////
////                // Extract username, password hash, and salt from the credentialStream
////                String credentials = credentialStream.toString("UTF-8");
////                String[] parts = credentials.split(";"); //";" is the delimiter
////                String username;
////                String passwordHash;
////                String streamedPassword;
////                String saltString;
////                String id;
////
////                if (parts.length == 3) {
////                    username = parts[0];
////                    streamedPassword = parts[1];
////                    saltString = parts[2];
////
////                    // Use the extracted values as needed
////                    System.out.println("Username: " + username);
////                    System.out.println("Password to Hash: " + streamedPassword);
////                    System.out.println("Salt: " + saltString);
////                } else {
////                    System.out.println("Invalid credentials format.");
////                    return;
////                }
////
////                String[] saltParts = saltString.substring(1, saltString.length() - 1).split(", ");
////                byte[] salt = new byte[saltParts.length];
////                for (int i = 0; i < saltParts.length; i++) {
////                    // Remove any non-numeric characters, such as "]" from the end of the string
////                    saltParts[i] = saltParts[i].replaceAll("[^\\d-]", "");
////                    salt[i] = Byte.parseByte(saltParts[i]);
////                }
////
////                String passwordDb = null;
////
////                if(database.userExists(username)) {
////                    passwordDb = database.hashedPassword(username);
////                    System.out.println("User found");
////                }
////                else{
////                    System.out.println("User not found.");
////                    return;
////                }
////
////                passwordHash = Arrays.toString(saltHash.hash(streamedPassword.toCharArray(), salt));
////
////                // Check if the credentials are valid
////                if (!passwordDb.equals(passwordHash)) {
////                    System.out.println("Invalid credentials");
////                    return;  // End the function if credentials are invalid
////                }
////                OutputStream outputStream = socket.getOutputStream();
////                outputStream.write('1');
////
////                System.out.println("Valid credentials received.");
////
////                System.out.println("Connection Accepted , receiving Command Packet");
////
////                //
////                //
////                //
////                //
////                //
////                ///////////////////////////////////////////////// COMMAND PACKET ///////////////////////////////////////////////////////////
////                ByteArrayOutputStream CommandPacketStream = new ByteArrayOutputStream();
////                int CoommandPacketBytes;
////
////                // Read the command packet until a newline character is encountered
////                while ((CoommandPacketBytes = inputStream.read()) != -1) {
////                    if ((char) CoommandPacketBytes == '\n') {
////                        break;
////                    }
////                    CommandPacketStream.write(CoommandPacketBytes);
////                }
////
////                // Convert the command packet to a string
////                String commandPacket = CommandPacketStream.toString("UTF-8").trim();
////
////                // Run regex to parse the command packet and extract its parts
////                String[] CommandPacketParts = commandPacket.split(";");
////
////                // Check the command and perform actions accordingly
////                if (CommandPacketParts[0].equals("ReceiveFile")) {
////                    // Perform actions for receiving a file
////                    //
////                    //
////                    //
////                    //
////                    //
////                    /////////////////////////////////////////////////// RECEIVE FILE ///////////////////////////////////////////////////////////
////
////                    // Receive file name
////                    ByteArrayOutputStream fileNameStream = new ByteArrayOutputStream();
////                    System.out.println("Initialized filenamestream");
////                    int byteRead;
////                    System.out.println("Initialized byteread");
////
////                    if (inputStream.available() > 0) {
////                        while ((byteRead = inputStream.read()) != -1) {
////                            System.out.print("Running while loop");
////                            if ((char) byteRead == '\n') break;
////                            fileNameStream.write(byteRead);
////                        }
////                    } else {
////                        System.out.println("No data available to stream or finished streaming name buffer");
////                    }
////
////                    while ((byteRead = inputStream.read()) != -1) {
////                        System.out.print("Writing name buffer\n");
////                        if ((char) byteRead == '\n') break;
////                        fileNameStream.write(byteRead);
////                    }
////                    String fileName = fileNameStream.toString("UTF-8");
////                    System.out.println("File Name : " + fileName);
////
////                    // Receive encrypted file data
////                    ByteArrayOutputStream encryptedDataStream = new ByteArrayOutputStream();
////                    byte[] buffer = new byte[1024];
////                    int bytesRead;
////                    while ((bytesRead = inputStream.read(buffer)) != -1) {
////                        encryptedDataStream.write(buffer, 0, bytesRead);
////                    }
////
////                    String key = new String(Files.readAllBytes(Paths.get("src/main/java/com/example/java_project/key.txt")));
////                    AESEncryptor aesEncryptor = new AESEncryptor(key);
////                    byte[] decryptedData = aesEncryptor.decryptData(encryptedDataStream.toByteArray());
////
////                    id = database.getUUID(username);
////                    Path path = Paths.get(String.valueOf(directory),String.valueOf(id));
////                    File pathDir = path.toFile();
////                    if (!pathDir.exists()) {
////                        pathDir.mkdirs();
////                    }
////                    // Write received file data to file
////                    try (FileOutputStream fileOutputStream = new FileOutputStream(new File(String.valueOf(path), fileName))) {
////                        fileOutputStream.write(decryptedData);
////                    }
////                    System.out.println("File received and decrypted successfully.");
////
////                }else if (CommandPacketParts[0].equals("DeleteUser")) {
////
////                    // Perform actions for creating a user
//////                    String DeleteUsername;
////                    String DeleteUsername = CommandPacketParts[1];
////                    database.deleteDb(DeleteUsername);
////
////                } else if (CommandPacketParts[0].equals("UpdateUser")) {
////
////                    // Perform actions for updating a user
////                    String oldUsername = CommandPacketParts[1];
////                    String newUsername = CommandPacketParts[3];
////                    String newPassword = CommandPacketParts[4];
////                    database.updateDb(oldUsername, newUsername, newPassword);
////
////                } else {
////                    System.out.println("Invalid command packet.");
////                }
////
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////
////    public static void main(String[] args) {
////
////        Database database = new Database();
////        database.establishConnection();
////
////        // Main initialization
////        String serverIp = "127.0.0.1";  // For local connection for testing purposes or LAN
////        int serverPort = 8080;
////        receiveFile(serverIp, serverPort, database);
////
////        // Close the database connection
////        database.closeConnection();
////    }
////}
//
//
//
public class FileReceiver {
    public static void receiveFile(String serverIp, int serverPort, Database database) {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Waiting for connection on port " + serverPort);

            File directory = new File("src/main/java/com/example/java_project/received_files");
            if (!directory.exists()) directory.mkdirs();

            try (Socket socket = serverSocket.accept();
                 InputStream inputStream = socket.getInputStream()) {
                System.out.println("Connection accepted from " + socket.getInetAddress());

                boolean keepConnectionOpen = true;
                boolean loggedIn = false;
                boolean receivingFiles = false;
                String loggedUser = null;

                while (keepConnectionOpen) {
                    database.establishConnection();
                    // Read the command packet until a newline character is encountered
                    ByteArrayOutputStream commandPacketStream = new ByteArrayOutputStream();
                    int commandPacketByte;
                    while ((commandPacketByte = inputStream.read()) != -1) {
                        if ((char) commandPacketByte == '\n') {
                            break;
                        }
                        commandPacketStream.write(commandPacketByte);
                    }

                    // Convert the command packet to a string
                    String commandPacket = commandPacketStream.toString("UTF-8").trim();
                    System.out.println(commandPacket);

                    // Run regex to parse the command packet and extract its parts
                    String[] commandPacketParts = commandPacket.split(";");

                    // Check the command and perform actions accordingly
                    if (commandPacketParts[0].equals("CreateUser") && !receivingFiles) {
                        // Perform actions for creating a user
//                        database.establishConnection();
                        System.out.println("Creating User");
                        database.createDb(commandPacketParts[1], commandPacketParts[2],serverIp,serverPort);
                        database.closeConnection();
                    } else if (commandPacketParts[0].equals("Login")) {
                        // Perform actions for deleting a user
//                        database.establishConnection();
//                        if(loggedIn){
//                            getFile.receiveFile("127.0.0.1", 5050, database);
//                            database.closeConnection();
//                        }
                        if (database.checkCredentials(commandPacketParts[1], commandPacketParts[2], commandPacketParts[3])) {
                            loggedIn = true;
                            loggedUser = commandPacketParts[1];
                            System.out.println("Logged in");
//                            getFile.receiveFile("127.0.0.1", 5050, database);
//                            database.closeConnection();

                        }

                    } else if (commandPacketParts[0].equals("Logout") && !receivingFiles) {
                        // Perform actions for updating a user
                        loggedIn = false;
                    }
                    else if (commandPacketParts[0].equals("ReceiveFile")) {
                        System.out.println("Receiving file {~FileReceiver}");
                        receivingFiles = true;
//                        database.establishConnection();
                        getFile.receiveFile("127.0.0.1", 5050, database); // Call the method from getFile class
                        database.closeConnection();

                        receivingFiles = false;

                    }
                    else if (commandPacketParts[0].equals("DeleteUser") && loggedIn) {

                        // Perform actions for deleting a user
//                        database.establishConnection();
                        database.deleteDb(commandPacketParts[1]);
                        database.closeConnection();

                    } else if (commandPacketParts[0].equals("UpdateUser") && loggedIn) {

                        // Perform actions for updating a user
//                        database.establishConnection();
                        database.updateDb(loggedUser, commandPacketParts[1], commandPacketParts[2]);
                        database.closeConnection();

                    } else if (commandPacketParts[0].equals("CloseConnection")) {

                        System.out.println("Closing connection.");
                        keepConnectionOpen = false;
                        database.closeConnection();
                        Platform.exit();

                    } else {
                        System.out.println("Unknown command packet");
                    }
                }
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