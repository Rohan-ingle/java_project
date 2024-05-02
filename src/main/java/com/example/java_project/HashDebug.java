package com.example.java_project;
import com.example.java_project.saltHash;

import java.util.Arrays;

public class HashDebug {
    public static void main(String[] args) {
        // Generate salt
        byte[] salt = saltHash.getSalt();

        // Password string
        String passwordString = "mySecretPassword";

        // Convert password string to char array
        char[] passwordChars = passwordString.toCharArray();

        // Hash the password
        byte[] hashedPassword = saltHash.hash(passwordChars, salt);

        // Print salt, hashed password, and original password
        System.out.println("Salt: " + Arrays.toString(salt));
        System.out.println("Hashed Password: " + Arrays.toString(hashedPassword));
        System.out.println("Original Password: " + passwordString);
    }
}
