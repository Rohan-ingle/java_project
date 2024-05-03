package com.example.java_project;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;

import com.example.java_project.Database;
import com.example.java_project.AESEncryptor;

public class FileReceiverFunction {

    public static void receiveFile(InputStream inputStream, String username, Database database) throws Exception {
        ByteArrayOutputStream fileNameStream = new ByteArrayOutputStream();
        int byteRead;

        if (inputStream.available() > 0) {
            while ((byteRead = inputStream.read()) != -1) {
                if ((char) byteRead == '\n') break;
                fileNameStream.write(byteRead);
            }
        } else {
            System.out.println("No data available to stream or finished streaming name buffer");
            return;
        }

        while ((byteRead = inputStream.read()) != -1) {
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

        String id = database.getUUID(username);
        Path directory = Paths.get("src/main/java/com/example/java_project/received_files", id);
        Files.createDirectories(directory);

        // Write received file data to file
        Path filePath = directory.resolve(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
            fileOutputStream.write(decryptedData);
        }
        System.out.println("File received and decrypted successfully.");
    }
}
