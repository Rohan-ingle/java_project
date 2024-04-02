import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RandomKeyGenerator {

    public static byte[] generateRandomKey(int keyLength) throws NoSuchAlgorithmException {
        // Create a SecureRandom object
        SecureRandom secureRandom = new SecureRandom();

        // Initialize the KeyGenerator for AES with the specified key length
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keyLength, secureRandom);

        // Generate a random AES key
        SecretKey secretKey = keyGenerator.generateKey();

        // Get the encoded bytes of the generated key
        return secretKey.getEncoded();
    }

    public static void writeToFile(String content, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, content.getBytes());
            System.out.println("Content written to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing content to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            // Specify the desired key length in bytes (e.g., 16 for AES-128, 24 for AES-192, 32 for AES-256)
            int keyLengthBytes = 128;

            // Generate a random key
            byte[] randomKey = generateRandomKey(keyLengthBytes);

            // Print the generated key as a hexadecimal string
            System.out.println("Random Key: " + bytesToHex(randomKey));
            String content = bytesToHex(randomKey);
            String filePath = "key.txt";
            writeToFile(content, filePath);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // Utility method to convert byte array to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

