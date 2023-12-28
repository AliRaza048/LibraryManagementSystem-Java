/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
* @author Bilal computer
 */
public class SQLConnection implements IConnection {

    private String serverName;
    private String dbName;
    private String dbUserName;
    private String dbUserPassword;
    
    SQLConnection(String sName, String dbName, String userId, String userPass) {
        this.serverName = sName;
        this.dbName = dbName;
        this.dbUserName = userId;
        this.dbUserPassword = userPass;
    }

    @Override
    public Connection getConnection() {
         try {
        // Correctly construct the connection string using the class variables
        String connectionUrl = "jdbc:mysql://" + this.serverName + ":3306/" + this.dbName
            + "?zeroDateTimeBehavior=CONVERT_TO_NULL&trustServerCertificate=true";
        return DriverManager.getConnection(connectionUrl, this.dbUserName, this.dbUserPassword);
    }
    catch (Exception e) {
        System.out.println("Error Trace in getConnection() : " + e.getMessage());
    }
    return null;
    }
}





//public class SQLConnection implements IConnection {
//
//    private String serverName;
//    private String dbName;
//    private String dbUserName;
//    private String dbUserPassword;
//    
//    SQLConnection(String sName, String dbName, String userId, String userPass) {
//        this.serverName = sName;
//        this.dbName = dbName;
//        this.dbUserName = userId;
//        this.dbUserPassword = userPass;
//    }
//
//    @Override
//    public Connection getConnection() {
//        try {
//            return DriverManager.getConnection("jdbc:sqlserver://;databaseName="+this.dbName+";trustServerCertificate=true;", this.dbUserName,this.dbUserPassword);
//        }
//        catch (Exception e) {
//            System.out.println("Error Trace in getConnection() : " + e.getMessage());
//        }
//        return null;
//    }
//}