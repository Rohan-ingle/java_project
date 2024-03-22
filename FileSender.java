import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class FileSender {

    public static void sendFile(String serverIp, int serverPort, String filePath) {
        try (Socket socket = new Socket(serverIp, serverPort);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             OutputStream outputStream = socket.getOutputStream()) {

            // Read key from file
            byte[] keyBytes = Files.readAllBytes(new File("key.txt").toPath());
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Send file name
            String fileName = new File(filePath).getName();
            outputStream.write(fileName.getBytes());
            outputStream.write('\n');

            // Send file data
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                outputStream.write(encryptedBytes);
            }
            byte[] encryptedBytes = cipher.doFinal();
            outputStream.write(encryptedBytes);

            System.out.println("File sent successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 5050;
        String filePath = "D:\\wallpapers\\anime-night-stars-sky-clouds-scenery-digital-art-4k-wallpaper-uhdpaper.com-772@0@i.jpg";
        sendFile(serverIp, serverPort, filePath);
    }
}
