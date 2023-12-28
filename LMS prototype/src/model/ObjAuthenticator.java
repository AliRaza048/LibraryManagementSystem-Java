/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;

/**
 *
* @author Bilal computer
 */
public class ObjAuthenticator {

    static void logout(Response objResponse) {
         try {
        // Perform logout operations, such as clearing session data, closing connections, etc.

        objResponse.messagesList.add(new Message("Logout successful!", MessageType.SUCCESS));
    } catch (Exception e) {
        objResponse.messagesList.add(new Message("An error occurred during logout.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private Connection dbConnection;
    
    //login
    
    public Response login(String username, String password) {
        Response objResponse = new Response();
        try {
            // Create a prepared statement to check the validity of username and password
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            PreparedStatement statement = dbConnection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            
            System.out.println("");
            // Check if the query returned a result
            
//            if (resultSet.next() && resultSet.getInt(1) > 0) {
//                // Login successful
//                
//                objResponse.messagesList.add(new Message("Login successful!", MessageType.SUCCESS));
//            } else {
//                // Invalid credentials
//                System.out.println("LOGGED IN");
//                objResponse.messagesList.add(new Message("Invalid username or password.", MessageType.ERROR));
//            }
            
            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            // Exception occurred during login
            objResponse.messagesList.add(new Message("An error occurred during login.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
        
        return objResponse;
    }
    
    public void closeConnection() {
        try {
            // Close the database connection
            dbConnection.close();
        } catch (Exception e) {
            // Exception occurred while closing the connection
            System.out.println("An error occurred while closing the database connection.");
            System.out.println(e.getMessage());
        }
    }
    

}