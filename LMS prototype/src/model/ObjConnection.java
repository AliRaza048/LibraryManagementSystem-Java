/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//Apply Singleton Pattern
public class ObjConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";
    private static Connection instance = null;

    // Private constructor to prevent instantiation
    private ObjConnection() {}

    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return instance;
    }
}
//Apply Singleton Pattern




//package model;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class ObjConnection {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
//    private static final String DB_USER = "your_username";
//    private static final String DB_PASSWORD = "your_password";
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//    }
//}

