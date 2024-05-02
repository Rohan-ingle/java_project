package com.example.java_project;

import java.sql.*;

public class Database {

    // Define connection as a class member to make it accessible throughout the Database class
    private Connection connection;

    public static void main(String[] args) {
        Database db = new Database();
        try {
            // Load the JDBC driver for the H2 database
            Class.forName("org.h2.Driver");

            // Create a connection to the database
            db.connection = DriverManager.getConnection("jdbc:h2:~/test", "admin", "admin");

            // Perform query
            db.performQuery();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed after usage
            try {
                if (db.connection != null) db.connection.close();
            } catch (SQLException e) {
                System.out.println("Failed to close the connection: " + e.getMessage());
            }
        }
    }

    public void performQuery() throws SQLException {
        // Create a statement to execute SQL queries
        Statement statement = connection.createStatement();

        // Execute a query and get the result set
        ResultSet resultSet = statement.executeQuery("SELECT 3*4");

        // Process the result set
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1)); // Print the result of 3*4
        }

        // Close all resources to avoid memory leaks
        resultSet.close();
        statement.close();
    }
}
