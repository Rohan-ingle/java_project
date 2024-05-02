package com.example.java_project;

import java.sql.SQLException;

public interface interfaceDb {
    void establishConnection() throws SQLException, ClassNotFoundException;

    void closeConnection() throws SQLException;

    void updateDb(String oldUsername, String newUsername, String newPassword) throws SQLException;

    void deleteDb(String username) throws SQLException;

    void createDb(String username, String hashedPassword) throws SQLException;

    String hashedPassword(String username) throws SQLException;

    String getUUID(String username) throws SQLException;

    boolean userExists(String username) throws SQLException;
}
