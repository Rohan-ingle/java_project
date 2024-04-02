import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.io.File;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class FileSenderGUI extends JFrame {
    private JTextField serverIpField, serverPortField, filePathField;
    private JButton sendButton;

    public FileSenderGUI() {
        setTitle("File Sender");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Server IP:"));
        serverIpField = new JTextField(20);
        add(serverIpField);

        add(new JLabel("Server Port:"));
        serverPortField = new JTextField(20);
        add(serverPortField);

        add(new JLabel("File Path:"));
        filePathField = new JTextField(20);
        add(filePathField);

        sendButton = new JButton("Send File");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFile();
            }
        });
        add(new JLabel("")); // Placeholder
        add(sendButton);
    }

    private void sendFile() {
        String serverIp = serverIpField.getText();
        int serverPort = Integer.parseInt(serverPortField.getText());
        String filePath = filePathField.getText();

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

            JOptionPane.showMessageDialog(this, "File sent successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileSenderGUI frame = new FileSenderGUI();
            frame.setVisible(true);
        });
    }
}
