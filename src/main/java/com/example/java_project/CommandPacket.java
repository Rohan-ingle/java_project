package com.example.java_project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class CommandPacket implements CommandPacketInterface {

    @Override
    public String Update(String Username, String Password, String NewUsername, String NewPassword, Socket socket) {

        saltHash hash = new saltHash();

        String Salt = Arrays.toString(hash.getSalt());
        String CredString = "UpdateUser"+ ";" +NewUsername + ";" + NewPassword+ ";" + Salt + "\n";
        SendCommand(socket, CredString);
        return CredString;
    }

    @Override
    public String Delete(String Username, Socket socket) {
        String DeleteString = "DeleteUser" + ";" + Username + ";" + "\n";
        SendCommand(socket, DeleteString);
        return DeleteString;
    }

    @Override
    public String Create(String Username, String Password, Socket socket) {

        String CreateString = "CreateUser" + ";" + Username + ";" + Password + "\n";
        SendCommand(socket, CreateString);
        return CreateString;
    }

    @Override
    public String sendFileCommand(String Username, String Password, Socket socket, String filepath){
        String SendFileCommand = "SendFile" + ";" + Username + ";" + Password + "\n";
        SendCommand(socket, SendFileCommand);
        SendFile(socket, filepath);
        return SendFileCommand;
    }

    @Override
    public void CloseConnection(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void SendCommand(Socket socket, String command){
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void SendFile(Socket socket, String filePath) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            // Read file content and send it in chunks
            byte[] buffer = new byte[1024];
            try (InputStream fileInputStream = new FileInputStream(filePath)) {
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void login(String Username, String Password, String salt, Socket socket) {
        String LoginString = "Login" + ";" + Username + ";" + Password+ ";" + salt + "\n";
        SendCommand(socket, LoginString);
    }
}
