package com.example.java_project;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

public class saltHash {
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final Random randomNo = new SecureRandom();

    public static byte[] getSalt(){
            byte[] salt = new byte[16];
            randomNo.nextBytes(salt);
            return salt;
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

//    public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
//        byte[] pwdHash = hash(password, salt);
//        Arrays.fill(password, Character.MIN_VALUE);
//        if (pwdHash.length != expectedHash.length) return false;
//        for (int i = 0; i < pwdHash.length; i++) {
//            if (pwdHash[i] != expectedHash[i]) return false;
//        }
//        return true;
//    }
}
