package com.example.java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSenderGUI extends JFrame {
    private JTextField serverIpField, serverPortField, filePathField;
    private JButton sendButton;

    //
    //
    //
    // Set UI size and title
    public FileSenderGUI() {
        setTitle("File Sender");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }


    //
    //
    //
    // Basic old UI init
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
        add(new JLabel(""));
        add(sendButton);
    }

    //
    //
    //
    // Most crucial function HARDCODED FOR TESTING JUST EDIT filepath AND PRESS SEND IN UI LEAVING IP AND PORT FIELD EMPTY
    private void sendFile() {
//        String serverIp = serverIpField.getText();
//        int serverPort = Integer.parseInt(serverPortField.getText());
//        String filePath = filePathField.getText();

        String serverIp = "127.0.0.1";
        int serverPort = 8080;
        String filePath = "D:\\wallpapers\\island-house-sunset-scenery-digital-art-4k-wallpaper-uhdpaper.com-269@0@j.jpg";
//        String login = "root;mySecretPassword;[-3, -30, -18, 105, 42, -86, -107, -109, 11, 38, -72, 64, 1, 39, -56, 26]";

        if ((filePath.startsWith("\"") && filePath.endsWith("\"")) || (filePath.startsWith("'") && filePath.endsWith("'"))) {
            filePath = filePath.substring(1, filePath.length() - 1);
        }

        File file = new File(filePath);

        //
        //
        //
        // REPLACE THIS WITH CUSTOM EXCEPTION
        if (!file.exists() || file.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Invalid file path or the path is a directory.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        //
        //
        //
        // try to achieve connection with a given socket [It was defined in the UI code as an input filed]
        try (Socket socket = new Socket(serverIp, serverPort);
//             FileInputStream fileInputStream = new FileInputStream(filePath);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

            // Read credentials from the credentials.txt file
            String credentials = new String(Files.readAllBytes(Path.of("src/main/java/com/example/java_project/cmdcred.txt")), "UTF-8");
            System.out.println(credentials);
            outputStream.write(credentials.getBytes());
            outputStream.write('\n');

            String key = null;

            // Read encryption key from key.txt but do not send it
            Path path = Paths.get("src/main/java/com/example/java_project/key.txt");
            if(!Files.exists(path)) {
                System.out.println("Key file doesn't exist");
//                key = "123";
            }
            else {
                key = new String(Files.readAllBytes(Path.of("src/main/java/com/example/java_project/key.txt")), "UTF-8");
            }

            // Wait to receive permission from server to send the file
            int permission = inputStream.read();  // Read one byte for permission

            // The server returns a string "1" if it accepts the credentials
            if (permission == '1') {  // Permission granted
                AESEncryptor aesEncryptor = new AESEncryptor(key);
                byte[] fileContent = Files.readAllBytes(Path.of(filePath));
                byte[] encryptedContent = aesEncryptor.encryptData(fileContent);  // Encrypt the file content

                // Send file name followed by a newline
                String fileName = new File(filePath).getName();
                outputStream.write(fileName.getBytes());
                outputStream.write('\n');

                // Send the encrypted content in chunks
                int offset = 0;
//                while (offset < encryptedContent.length) {
//                    int length = Math.min(encryptedContent.length - offset, 1024);
//                    outputStream.write(encryptedContent, offset, length);
//                    offset += length;
//                }
                int totalLength = encryptedContent.length;

                //
                //
                //
                //
                // The try catch below can be used to make a progress bar and ETA (USE IN UI)
                while (offset < totalLength) {
                    int length = Math.min(totalLength - offset, 1024);
                    try {
                        outputStream.write(encryptedContent, offset, length);
                        System.out.println("Sent " + (offset + length) + " of " + totalLength + " bytes.");
                    } catch (IOException e) {
                        System.out.println("Failed to send data at offset: " + offset + ". Retrying...");
                        continue; // Add a retry limit or backoff strategy
                    }
                    offset += length;
                }

            } else {
                JOptionPane.showMessageDialog(this, "Permission denied by server.");
                return;
            }

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








//
//package com.example.java_project;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.*;
//import java.net.Socket;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//public class FileSenderGUI extends JFrame {
//    private JTextField serverIpField, serverPortField, filePathField;
//    private JButton sendButton;
//    private JComboBox<String> commandComboBox;
//
//    // Set UI size and title
//    public FileSenderGUI() {
//        setTitle("File Sender");
//        setSize(400, 250);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        initUI();
//    }
//
//    // Basic UI initialization
//    private void initUI() {
//        setLayout(new GridLayout(5, 2));
//
//        add(new JLabel("Server IP:"));
//        serverIpField = new JTextField(20);
//        add(serverIpField);
//
//        add(new JLabel("Server Port:"));
//        serverPortField = new JTextField(20);
//        add(serverPortField);
//
//        add(new JLabel("File Path:"));
//        filePathField = new JTextField(20);
//        add(filePathField);
//
//        add(new JLabel("Command:"));
//        String[] commands = {"ReceiveFile", "CreateUser", "DeleteUser", "UpdateUser"};
//        commandComboBox = new JComboBox<>(commands);
//        add(commandComboBox);
//
//        sendButton = new JButton("Send Command and File");
//        sendButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                sendCommandAndFile();
//            }
//        });
//        add(new JLabel(""));
//        add(sendButton);
//    }
//
//    // Method to send the command packet and file
//    private void sendCommandAndFile() {
//        String serverIp = serverIpField.getText();
//        int serverPort = Integer.parseInt(serverPortField.getText());
//        String filePath = filePathField.getText();
//        String selectedCommand = (String) commandComboBox.getSelectedItem(); // Get selected command
//
//        // Check if the file path is enclosed in quotes and remove them
//        if ((filePath.startsWith("\"") && filePath.endsWith("\"")) || (filePath.startsWith("'") && filePath.endsWith("'"))) {
//            filePath = filePath.substring(1, filePath.length() - 1);
//        }
//
//        File file = new File(filePath);
//
//        // Check if the file exists and is not a directory
//        if (!file.exists() || file.isDirectory()) {
//            JOptionPane.showMessageDialog(this, "Invalid file path or the path is a directory.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try (Socket socket = new Socket(serverIp, serverPort);
//             OutputStream outputStream = socket.getOutputStream();
//             InputStream inputStream = socket.getInputStream()) {
//
//            // Send the selected command packet to the server
//            outputStream.write(selectedCommand.getBytes());
//            outputStream.write('\n');
//
//            // Depending on the command, send additional parameters
////            switch (selectedCommand) {
////                case "ReceiveFile":
////                    // No additional parameters needed for ReceiveFile command
////                    break;
////                case "CreateUser":
////                case "DeleteUser":
////                case "UpdateUser":
////                    // Additional parameters for CreateUser, DeleteUser, and UpdateUser commands
////                    String[] params = filePath.split(";");
////                    if (params.length < 3) {
////                        JOptionPane.showMessageDialog(this, "Invalid parameters for the command.", "Error", JOptionPane.ERROR_MESSAGE);
////                        return;
////                    }
////                    for (String param : params) {
////                        outputStream.write(param.getBytes());
////                        outputStream.write(';'); // Delimiter between parameters
////                    }
////                    outputStream.write('\n'); // End of parameters
////                    break;
////                default:
////                    JOptionPane.showMessageDialog(this, "Invalid command.", "Error", JOptionPane.ERROR_MESSAGE);
////                    return;
////            }
//
//            // Read permission from the server
//            int permission = inputStream.read();
//
//            // Check if the server grants permission to send the file
//            if (permission == '1') {
//                // Proceed to send the file
//                sendFile(socket, filePath, outputStream);
//            } else {
//                JOptionPane.showMessageDialog(this, "Permission denied by server.");
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    // Method to send the file to the server
//    private void sendFile(Socket socket, String filePath, OutputStream outputStream) {
//        try {
//            // Read file content and send it in chunks
//            byte[] buffer = new byte[1024];
//            try (InputStream fileInputStream = new FileInputStream(filePath)) {
//                int bytesRead;
//                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//
//            JOptionPane.showMessageDialog(this, "File sent successfully.");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error sending file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        } finally {
//            try {
//                socket.close(); // Close the socket after sending the file
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            FileSenderGUI frame = new FileSenderGUI();
//            frame.setVisible(true);
//        });
//    }
//}
