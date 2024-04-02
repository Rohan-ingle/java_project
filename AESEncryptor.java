
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptor implements Encryptable {
    private SecretKeySpec secretKeySpec;

    public AESEncryptor(byte[] key) {
        this.secretKeySpec = new SecretKeySpec(key, "AES");
    }

    @Override
    public byte[] encryptData(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Perform the encryption
        byte[] encryptedData = cipher.doFinal(data);

        return encryptedData;
    }

    @Override
    public byte[] decryptData(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        // Decrypt the data
        byte[] decryptedData = cipher.doFinal(data);

        // Return the decrypted data
        return decryptedData;
    }
}

