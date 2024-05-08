package com.example.java_project;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class CommandPacket implements CommandPacketInterface {

    @Override
    public String Update(String Username, String Password, String NewUsername, String NewPassword, Socket socket) {
//        saltHash Hash = new saltHash();
//
//        byte[] Salt = Hash.getSalt();
//        byte[] NewPasswordHashedBytes = Hash.hash(NewPassword.toCharArray(), Salt);
//        String NewPasswordHashed = bytesToHex(NewPasswordHashedBytes); // Convert byte array to hexadecimal string
//        String saltString = bytesToHex(Salt); // Convert salt byte array to hexadecimal string

        String CredString = "UpdateUser" + ";" + NewUsername + ";" + NewPassword + ";" + NewPassword + "\n";
        SendCommand(socket, CredString);
        System.out.println("New Creds Hashed: " + CredString);
        return CredString;
    }

//    public static byte[] stringToByte(String str) {
//        byte[] byteArray = new byte[str.length()];
//        for (int i = 0; i < str.length(); i++) {
//            byteArray[i] = (byte) str.charAt(i); // Convert char to byte
//        }
//        return byteArray;
//    }
//
//    public static String bytesToHex(byte[] bytes) {
//        StringBuilder result = new StringBuilder();
//        for (byte b : bytes) {
//            result.append(String.format("%02X", b));
//        }
//        return result.toString();
//    }


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
    public String sendFileCommand(Socket socket){
        String SendFileCommand = "ReceiveFile" + ";" + "\n";
        SendCommand(socket, SendFileCommand);
        return "SendFileCommand";
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
    public void SendFile(String filePath) throws ConnectException{

        fileSenderNoGUI sendfile = new fileSenderNoGUI();

        sendfile.sendFile("127.0.0.1", 5050, filePath);

    }

    @Override
    public void login(String Username, String Password, String salt, Socket socket) {
        String LoginString = "Login" + ";" + Username + ";" + Password+ ";" + salt + "\n";
        SendCommand(socket, LoginString);
    }

    @Override
    public String CloseConnection(Socket socket){
        String SendFileCommand = "CloseConnection" + ";" + "\n";
        SendCommand(socket, SendFileCommand);
        return "CloseConnection";
    }
}
