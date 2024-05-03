package com.example.java_project;

import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class Database implements interfaceDb {

    // Define connection as a class member to make it accessible throughout the Database class
    private Connection connection;
    private static final String DB_FILE_PATH = "./src/main/java/com/example/java_project/database";


    @Override
    public void establishConnection() {
        try {
            String url = "jdbc:h2:" + DB_FILE_PATH;

            // Load the H2 driver
            Class.forName("org.h2.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(url, "admin", "admin");
            Statement statement = connection.createStatement();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "username VARCHAR(255) UNIQUE," +
                    "password_hash VARCHAR(255)" +
                    ")";
            statement.executeUpdate(createTableSQL);
            System.out.println("Table created");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close the connection: " + e.getMessage());
        }
    }

    @Override
    public void updateDb(String oldUsername, String newUsername, String newPassword) {
        try {
            // Update the username and password in the database
            String updateSql = "UPDATE Users SET username = ?, password_hash = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);
                preparedStatement.setString(3, oldUsername);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User updated successfully");
                } else {
                    System.out.println("User with username '" + oldUsername + "' not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDb(String username) {
        try {
            // Delete the user from the database
            String deleteSql = "DELETE FROM Users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
                preparedStatement.setString(1, username);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User deleted successfully");
                } else {
                    System.out.println("User with username '" + username + "' not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createDb(String username, String hashedPassword) {
        try {
            // Check if the user already exists
            String query = "SELECT COUNT(*) AS count FROM Users WHERE username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    if (count > 0) {
                        System.out.println("User already exists");
                        return; // Exit the method if the user already exists
                    }
                }
            }

            // Generate a unique ID for the user
            String userId = UUID.randomUUID().toString();

            // Insert the user into the database
            String insertUserSQL = "INSERT INTO Users (id, username, password_hash) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, hashedPassword);
                preparedStatement.executeUpdate();
                System.out.println("User created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String hashedPassword(String username) throws SQLException {
        String passwordHash = null;
        String query = "SELECT password_hash FROM Users WHERE username = ?";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                passwordHash = resultSet.getString("password_hash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return passwordHash;
    }

    @Override
    public String getUUID(String username) throws SQLException {
        String uuid = null;
        String query = "SELECT id FROM Users WHERE username = ?";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                uuid = resultSet.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return uuid;
    }

    @Override
    public boolean userExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false by default if an exception occurs
    }

    @Override
    public boolean checkCredentials(String username, String password, String salt) {
        // Hash the provided password using the provided salt
        saltHash salthash = new saltHash();

        // Split the salt string into individual parts
        String[] saltParts = salt.substring(1, salt.length() - 1).split(", ");

        // Initialize a byte array to hold the salt
        byte[] saltBytes = new byte[saltParts.length];

        // Convert each salt part to a byte value
        for (int i = 0; i < saltParts.length; i++) {
            // Remove any non-numeric characters from the salt part
            saltParts[i] = saltParts[i].replaceAll("[^\\d-]", "");
            System.out.println("Salt part [" + i + "]: " + saltParts[i]);

            // Parse the salt part to obtain the byte value
            saltBytes[i] = Byte.parseByte(saltParts[i]);
            System.out.println("Salt byte [" + i + "]: " + saltBytes[i]);
        }

        // Hash the password using the parsed salt
        byte[] providedHash = salthash.hash(password.toCharArray(), saltBytes);
        System.out.println("Provided hash: " + Arrays.toString(providedHash));

//         Retrieve the stored hashed password from the database
        String query = "SELECT password_hash FROM Users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            System.out.println("Query: " + query);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Attempting get string");
                    String storedHashedPassword = resultSet.getString("password_hash");
                    System.out.println("Stored hashed password: " + storedHashedPassword);

                    // Convert the stored hashed password from Base64 string to byte array
                    System.out.println("Stored hash: " + storedHashedPassword);


                    // Check if the provided password hash matches the stored hashed password
                    boolean match = storedHashedPassword.equals(Arrays.toString(providedHash));
                    System.out.println("Password match: " + match);
                    return match;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
