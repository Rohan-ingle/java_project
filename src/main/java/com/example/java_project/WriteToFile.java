package com.example.java_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteToFile {

    public WriteToFile(String username, String passwordHash, String salt) {
        String credentials = username + "," + passwordHash + "," + salt + "\n";
        try {
            Files.write(Paths.get("src/main/java/com/example/java_project/Credentials.txt"), credentials.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
