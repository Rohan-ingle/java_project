import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReceiver {

    public static void receiveFile(String serverIp, int serverPort) {
        try {
            byte[] key = Files.readAllBytes(Paths.get("key.txt"));
            // byte[] key = "".getBytes();
            try (ServerSocket serverSocket = new ServerSocket(serverPort);
                 Socket socket = serverSocket.accept();
                 InputStream inputStream = socket.getInputStream()) {

                // Read key from file
                SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

                // Receive file name
                ByteArrayOutputStream fileNameStream = new ByteArrayOutputStream();
                int byteRead;
                while ((byteRead = inputStream.read()) != -1) {
                    if ((char) byteRead == '\n') {
                        break;
                    }
                    fileNameStream.write(byteRead);
                }
                String fileName = fileNameStream.toString();

                // Receive encrypted file data
                ByteArrayOutputStream fileDataStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                    fileDataStream.write(decryptedBytes);
                }
                byte[] decryptedBytes = cipher.doFinal();
                fileDataStream.write(decryptedBytes);

                // Write received file data to file
                try (FileOutputStream fileOutputStream = new FileOutputStream("./received_files/" + fileName)) {
                    fileOutputStream.write(fileDataStream.toByteArray());
                }

                System.out.println("File received and decrypted successfully.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 5050;
        while (true){
            System.out.println("Waiting for connection");
            receiveFile(serverIp, serverPort);
        }
    }
}