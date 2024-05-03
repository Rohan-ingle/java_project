package com.example.java_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class GenerateKeySalt {

    public void generate(String username, String passwordString){
        // Generate salt
        byte[] salt = saltHash.getSalt();

        // Convert password string to char array
        char[] passwordChars = passwordString.toCharArray();

        // Hash the password
        byte[] hashedPassword = saltHash.hash(passwordChars, salt);

        // Print salt, hashed password, and original password
        System.out.println("Salt: " + Arrays.toString(salt));
        System.out.println("Hashed Password: " + Arrays.toString(hashedPassword));
        System.out.println("Original Password: " + passwordString);

        String credentials = username + ";" + passwordString + ";" + Arrays.toString(salt) + "\n";
        try {
            Files.write(Paths.get("src/main/java/com/example/java_project/Credentials.txt"), credentials.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
