/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.dto.BookDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;
import model.dto.UserDTO;
import dal.IConnection;

import dal.SQLConnection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import model.dto.FeedbackDTO;
import model.dto.PaymentDTO;



public class RecordsAdder {
    
    private IConnection objConnection;

    public RecordsAdder() {
      
    }
    //login
    boolean login(String username, String password, Connection dbConnection) {
    try {
        PreparedStatement p = dbConnection.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?");
        p.setString(1, username);
        p.setString(2, password);
        ResultSet rs = p.executeQuery();

        if (rs.next()) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid username or password!");
            return false;
        }
    } catch (SQLException e) {
        System.out.println("An error occurred while logging in:");
        e.printStackTrace();
        return false;
    }
}

    //search
    void searchBook(String searchQuery, Response objResponse, Connection dbConnection) {
    try {
        PreparedStatement p = dbConnection.prepareStatement("SELECT * FROM Books WHERE name LIKE ? OR ISBN LIKE ?");
        p.setString(1, "%" + searchQuery + "%");
        p.setString(2, "%" + searchQuery + "%");
        ResultSet resultSet = p.executeQuery();

        while (resultSet.next()) {
            String ISBN = resultSet.getString("ISBN");
            String name = resultSet.getString("name");
            String authorname = resultSet.getString("authorname");
            String category = resultSet.getString("category");
            String publishername = resultSet.getString("publishername");
        }

        if (objResponse.messagesList.isEmpty()) {
            objResponse.messagesList.add(new Message("No books found.", MessageType.ERROR));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Ooops! Failed to search for books. Please contact support.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}
    
    //returnbook
    boolean returnBook(String bookId, Connection dbConnection) {
    try {
        PreparedStatement statement = dbConnection.prepareStatement("UPDATE Books SET status = 'Returned' WHERE book_id = ?");
        statement.setString(1, bookId);
        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Book returned successfully!");
            return true;
        } else {
            System.out.println("Failed to return the book. Please check the book ID.");
            return false;
        }
    } catch (SQLException e) {
        System.out.println("An error occurred during book return:");
        e.printStackTrace();
        return false;
    }
}


    //generatefine
        public void generateFine(String searchQuery, Response objResponse, Connection dbConnection) {
        try {
            PreparedStatement p = dbConnection.prepareStatement("SELECT * FROM Books WHERE name LIKE ? OR ISBN LIKE ?");
            p.setString(1, "%" + searchQuery + "%");
            p.setString(2, "%" + searchQuery + "%");
            ResultSet resultSet = p.executeQuery();

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String name = resultSet.getString("name");
                String authorname = resultSet.getString("authorname");
                String category = resultSet.getString("category");
                String publishername = resultSet.getString("publishername");

                // Calculate fine based on due date and current date
                LocalDate dueDate = resultSet.getDate("dueDate").toLocalDate();
                LocalDate currentDate = LocalDate.now();
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, currentDate);
                double fineAmount = daysOverdue * 0.5; // Assuming a fine of $0.50 per day

                // Add the fine information to the response
                String fineMessage = "Fine for book \"" + name + "\" (ISBN: " + ISBN + "): $" + fineAmount;
                objResponse.messagesList.add(new Message(fineMessage, MessageType.INFO));
            }

            if (objResponse.messagesList.isEmpty()) {
                objResponse.messagesList.add(new Message("No books found.", MessageType.ERROR));
            }
        } catch (SQLException e) {
            objResponse.messagesList.add(new Message("Ooops! Failed to search for books. Please contact support.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
    

void addBook(BookDTO objBook, Response objResponse, Connection dbConnection) {
    
    try {
        PreparedStatement p = dbConnection.prepareStatement("INSERT INTO Book (ISBN, title, authorname, publishername, category, status) VALUES (?, ?, ?, ?, ?, ?);");
        p.setString(1, objBook.ISBN);
        p.setString(2, objBook.name);
        p.setString(3, objBook.authorname);
        p.setString(4, objBook.publishername);
        p.setString(5, objBook.category);
        p.setString(6, "available");
        
        int rowsInserted = p.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Book Added Successfully", "Book Addition", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        if (e.getSQLState().equals("23000") && e.getErrorCode() == 2627) {
            JOptionPane.showMessageDialog(null, "BOOK with ISBN " + objBook.ISBN + " already exists.", "Duplicate ISBN", JOptionPane.WARNING_MESSAGE);
        } else if (e.getErrorCode() == 0) {
            JOptionPane.showMessageDialog(null, "UNABLE TO CONNECT TO DATABASE", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Ooops! Failed to add book. Please contact support for assistance", "Book Addition Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
        }
    }
}

     public void borrowBook(String bookISBN, String userId, Response objResponse, Connection dbConnection) {
        try {
            System.out.println("i am borrow book record modifier");
            // Check if the book exists
            PreparedStatement checkBook = dbConnection.prepareStatement("SELECT * FROM Books WHERE ISBN = ?;");
            checkBook.setString(1, bookISBN);
            if (checkBook.executeQuery().next()) {
                // Check if the user exists
                PreparedStatement checkUser = dbConnection.prepareStatement("SELECT * FROM Users WHERE id = ?;");
                checkUser.setString(1, userId);
                if (checkUser.executeQuery().next()) {
                    // Update the book's availability status
                    PreparedStatement borrowBook = dbConnection.prepareStatement("UPDATE Books SET availability = 'Unavailable' WHERE ISBN = ?;");
                    borrowBook.setString(1, bookISBN);
                    int rowsUpdated = borrowBook.executeUpdate();
                    if (rowsUpdated > 0) {
                        objResponse.messagesList.add(new Message("Book borrowed successfully.", MessageType.NOTIFICATION));
 }
                } else {
                    objResponse.messagesList.add(new Message("User with ID " + userId + " does not exist.", MessageType.WARNING));
                }
            } else {
                objResponse.messagesList.add(new Message("Book with ISBN " + bookISBN + " does not exist.", MessageType.WARNING));
            }
        } catch (SQLException e) {
            objResponse.messagesList.add(new Message("Oops! Failed to borrow the book. Please contact support for assistance.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
     
   public void registerAccount(UserDTO objUser, Connection dbConnection) {
    try {
        // Check if the user with the same email already exists
        PreparedStatement checkUser = dbConnection.prepareStatement("SELECT * FROM Users WHERE email = ?;");
        checkUser.setString(1, objUser.getEmail());
        ResultSet userResultSet = checkUser.executeQuery();

        if (userResultSet.next()) {
            JOptionPane.showMessageDialog(null, "User with the same email already exists.", "Account Registration", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Insert the new user into the database table 'Users'
        PreparedStatement insertUser = dbConnection.prepareStatement("INSERT INTO Users (username, email, password, gender, status) VALUES (?, ?, ?, ?, ?);");
        insertUser.setString(1, objUser.getUsername());
        insertUser.setString(2, objUser.getEmail());
        insertUser.setString(3, objUser.getPassword());
        insertUser.setString(4, objUser.getGender());
        insertUser.setString(5, objUser.getStatus());

        int rowsInserted = insertUser.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Account registered successfully.", "Account Registration", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to register the account. Please contact support for assistance.", "Account Registration Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Oops! Failed to register the account. Please contact support for assistance.", "Account Registration Error", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
    }
}


   
   public void addFeedback(FeedbackDTO feedback, Connection dbConnection) {
    try {
        System.out.println("I am addFeedback method in RecordsAdder");
        PreparedStatement p = dbConnection.prepareStatement("INSERT INTO Feedback (kindofcomment, aboutlibrary, comments, email, phoneno) VALUES (?, ?, ?, ?, ?);");
        p.setString(1, feedback.getKind());
        p.setString(2, feedback.getAbout());
        p.setString(3, feedback.getComments());
        p.setString(4, feedback.getEmail());
        p.setString(5, feedback.getPhoneNumber());
        int rowsInserted = p.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Feedback added successfully", "Feedback Addition", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add feedback. Please contact support for assistance", "Feedback Addition Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Oops! Failed to add feedback. Please contact support for assistance", "Feedback Addition Error", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
    }
}


  public void addPayment(PaymentDTO payment, Response response, Connection dbConnection) {
    try {
        PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO Payments (paymentId, amount, paymentDate) VALUES (?, ?, ?);");
        statement.setString(1, payment.getPaymentId());
        statement.setDouble(2, payment.getAmount());
        statement.setDate(3, java.sql.Date.valueOf(payment.getPaymentDate()));
        
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            response.setSuccess(true);
            response.getMessagesList().add(new Message("Payment added successfully.", MessageType.NOTIFICATION));
        } else {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("Failed to add payment.", MessageType.ERROR));
        }
    } catch (SQLException e) {
        response.setSuccess(false);
        response.getMessagesList().add(new Message("Oops! Failed to add payment. Please contact support for assistance.", MessageType.ERROR));
        response.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}
 
  public void deleteAccount(String username, Response objResponse, Connection dbConnection) {
    try {
        PreparedStatement p = dbConnection.prepareStatement("DELETE FROM Users WHERE username = ?");
        p.setString(1, username);
        int rowsDeleted = p.executeUpdate();
        if (rowsDeleted > 0) {
            objResponse.getMessagesList().add(new Message("Account deleted successfully.", MessageType.NOTIFICATION));
        } else {
            objResponse.getMessagesList().add(new Message("Failed to delete the account. Please contact support for assistance.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.getMessagesList().add(new Message("Oops! Failed to delete the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.getMessagesList().add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

//logout
    boolean logout() {
    try {
        // Perform logout operations, such as clearing session data, closing connections, etc.

        System.out.println("Logout successful!");
        return true;
    } catch (Exception e) {
        System.out.println("An error occurred during logout:");
        e.printStackTrace();
        return false;
    }
}

  
}
