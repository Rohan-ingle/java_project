package encryption;

public interface Encryptable {
    byte[] encryptData(byte[] data) throws Exception;
    byte[] decryptData(byte[] data) throws Exception;
}
