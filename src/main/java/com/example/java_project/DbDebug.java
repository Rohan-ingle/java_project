package com.example.java_project;
import com.example.java_project.Database;

import java.sql.SQLException;

public class DbDebug {
    public static void main(String[] args) throws SQLException {
        String password;
        String uuid;
        Database db = new Database();
        db.establishConnection();
        db.createDb("root","[-34, 78, -108, 78, -19, -70, -23, 56, 19, -15, -38, -66, 125, -54, 34, 115, 30, -53, 127, -35, 58, 73, 61, 51, -6, -38, -15, 19, -76, -78, -16, -21]");
        uuid = db.getUUID("Admin");
        System.out.println(uuid);
        password = db.hashedPassword("Admin");
        if(db.userExists("Jignesh")){
            System.out.println("User exists");
        }
        else{
            System.out.println("User doesnt exist");
        }
        System.out.println(password);
        db.closeConnection();
    }
}

//
//
// [Solved]
// Uncommenting the createDb shows unique key violation exception