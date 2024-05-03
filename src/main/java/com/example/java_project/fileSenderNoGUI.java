package com.example.java_project;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class fileSenderNoGUI {

    public static void sendFile(String serverIp, int serverPort, String filePath) {
        try (Socket socket = new Socket(serverIp, serverPort);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

            // Read credentials from the credentials.txt file
            String credentials = new String(Files.readAllBytes(Path.of("src/main/java/com/example/java_project/cmdcred.txt")), "UTF-8");
            System.out.println(credentials);
            outputStream.write(credentials.getBytes());
            outputStream.write('\n');

            String key = null;

            // Read encryption key from key.txt but do not send it
            Path path = Paths.get("src/main/java/com/example/java_project/key.txt");
            if(!Files.exists(path)) {
                System.out.println("Key file doesn't exist");
            }
            else {
                key = new String(Files.readAllBytes(Path.of("src/main/java/com/example/java_project/key.txt")), "UTF-8");
            }

            // Wait to receive permission from server to send the file
            int permission = inputStream.read();  // Read one byte for permission

            // The server returns a string "1" if it accepts the credentials
            if (permission == '1') {  // Permission granted
                AESEncryptor aesEncryptor = new AESEncryptor(key);
                byte[] fileContent = Files.readAllBytes(Path.of(filePath));
                byte[] encryptedContent = aesEncryptor.encryptData(fileContent);  // Encrypt the file content

                // Send file name followed by a newline
                String fileName = new File(filePath).getName();
                outputStream.write(fileName.getBytes());
                outputStream.write('\n');

                // Send the encrypted content in chunks
                int offset = 0;
                int totalLength = encryptedContent.length;
                while (offset < totalLength) {
                    int length = Math.min(totalLength - offset, 1024);
                    outputStream.write(encryptedContent, offset, length);
                    offset += length;
                }
            } else {
                System.out.println("Permission denied by server.");
            }

            System.out.println("File sent successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error sending file: " + ex.getMessage());
        }
    }

//    public static void main(String[] args) {
//        String serverIp = "127.0.0.1";
//        int serverPort = 5050;
//        String filePath = "D:\\wallpapers\\island-house-sunset-scenery-digital-art-4k-wallpaper-uhdpaper.com-269@0@j.jpg";
//
//        sendFile(serverIp, serverPort, filePath);
//    }
}
