package com.example.java_project;

import java.net.Socket;

public interface CommandPacketInterface {

    String Update(String Username, String Password, String NewUsername, String NewPassword, Socket socket);

    String Delete(String Username, Socket socket);

    String Create(String Username, String Password, Socket socket);

    String sendFileCommand(String Username, String Password, Socket socket, String filepath);

    void CloseConnection(Socket socket);

    void SendCommand(Socket socket, String command);

    void SendFile(Socket socket, String filePath);

    void login(String Username, String Password, String salt, Socket socket);
}
