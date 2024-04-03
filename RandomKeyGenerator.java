import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Extend the provided FileOperator class
public class RandomKeyGenerator extends FileOperator {
    private int keyLength;

    // Constructor that takes the file path for the key, the AESEncryptor, and the desired key length
    public RandomKeyGenerator(String filePath, AESEncryptor encryptor, int keyLength) {
        super(filePath, encryptor);
        this.keyLength = keyLength; // AES key length (e.g., 128, 192, 256)
    }

    // Implementation of the abstract operateFile method
    @Override
    void operateFile() throws Exception {
        byte[] randomKey = generateRandomKey(keyLength); // Generate the AES key
        System.out.println("Random Key: " + bytesToHex(randomKey));
        writeToFile(bytesToHex(randomKey), this.filePath); // Write the key to the specified file
    }

    // Generate a random AES key
    private byte[] generateRandomKey(int keyLength) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keyLength, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    // Write the hexadecimal representation of the key to the file
    private void writeToFile(String content, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content.getBytes());
        System.out.println("Content written to file successfully.");
    }

    // Convert byte array to a hexadecimal string
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Main method for testing
    public static void main(String[] args) {
        String filePath = "key.txt"; // Destination file for the key
        int keyLength = 128; // AES key length

        // Generate a temporary key for AESEncryptor instantiation
        byte[] tempKey = new byte[keyLength / 8]; // Divide by 8 to convert bits to bytes
        new SecureRandom().nextBytes(tempKey); // Fill tempKey with random bytes

        // Instantiate AESEncryptor with the temporary key
        AESEncryptor encryptor = new AESEncryptor(tempKey);

        RandomKeyGenerator keyGenerator = new RandomKeyGenerator(filePath, encryptor, keyLength);
        try {
            keyGenerator.operateFile(); // Generate the key and write to the file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
