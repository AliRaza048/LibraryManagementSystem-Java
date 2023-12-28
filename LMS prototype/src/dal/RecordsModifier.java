/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import model.dto.BorrowBookDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.OrderDTO;
import model.dto.Response;

/**
 *
* @author Bilal computer
 */
public class RecordsModifier {

    
    //login
    void login(String username, String password, Response objResponse, Connection dbConnection) {
    try {
        PreparedStatement p;
        p = dbConnection.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?");
        p.setString(1, username);
        p.setString(2, password);
        
        ResultSet resultSet = p.executeQuery();
        
        if (resultSet.next()) {
            objResponse.messagesList.add(new Message("Login successful.", MessageType.NOTIFICATION));
        } else {
            objResponse.messagesList.add(new Message("Invalid username or password.", MessageType.ERROR));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Oops! Failed to authenticate user. Please try again later.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

      //search
    void searchBook(String name, String isbn, Response objResponse, Connection dbConnection) {
    try {
        PreparedStatement p = dbConnection.prepareStatement("SELECT * FROM Books WHERE Name LIKE ? AND ISBN = ?");
        p.setString(1, "%" + name + "%");
        p.setString(2, isbn);
        ResultSet resultSet = p.executeQuery();

        if (resultSet.next()) {
            // Book found
            String bookName = resultSet.getString("Name");
            String bookISBN = resultSet.getString("ISBN");
            // Additional book attributes can be retrieved here

            objResponse.messagesList.add(new Message("Book found: " + bookName + ", ISBN: " + bookISBN, MessageType.NOTIFICATION));
        } else {
            // Book not found
            objResponse.messagesList.add(new Message("Book not found.", MessageType.NOTIFICATION));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Oops! Failed to search for the book. Please contact support for assistance.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n", MessageType.EXCEPTION));
        e.printStackTrace();
    }
}

    //return book
    
  

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
            objResponse.messagesList.add(new Message("Ooops! Failed to generate fine. Please contact support.", MessageType.ERROR));
            objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
        }
    }
     //logout
     void logout(Response objResponse) {
    try {
        // Perform logout operations, such as clearing session data, closing connections, etc.

        objResponse.messagesList.add(new Message("Logout successful!", MessageType.SUCCESS));
    } catch (Exception e) {
        objResponse.messagesList.add(new Message("An error occurred during logout.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}


public void returnBook(String bookId, Response objResponse, Connection dbConnection) {
    try {
        // Check if the book exists
        PreparedStatement checkBook = dbConnection.prepareStatement("SELECT * FROM Book WHERE isbn = ?;");
        checkBook.setString(1, bookId);
        ResultSet bookResultSet = checkBook.executeQuery();

        if (!bookResultSet.next()) {
            JOptionPane.showMessageDialog(null, "Book with ID " + bookId + " does not exist.", "Book Return", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the return date of the borrowed book
        PreparedStatement getReturnDate = dbConnection.prepareStatement("SELECT returnDate FROM Borrowed_Books WHERE isbn = ?;");
        getReturnDate.setString(1, bookId);
        ResultSet returnDateResultSet = getReturnDate.executeQuery();

        if (!returnDateResultSet.next()) {
            JOptionPane.showMessageDialog(null, "The book with ID " + bookId + " has not been borrowed.", "Book Return", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate returnDate = returnDateResultSet.getDate("returnDate").toLocalDate();
        LocalDate currentDate = LocalDate.now();

        // Calculate the number of days overdue
        long daysOverdue = ChronoUnit.DAYS.between(returnDate, currentDate);
        if (daysOverdue <= 0) {
            JOptionPane.showMessageDialog(null, "The book with ID " + bookId + " has been returned on time.", "Book Return", JOptionPane.INFORMATION_MESSAGE);
            updateBookStatus(bookId, "Available", dbConnection);
            deleteFromBorrowedBooks(bookId, dbConnection);
            return;
        }

        // Calculate the fine amount
        double fineAmount = daysOverdue * 100.0; // Assuming a fine of Rs. 100 per day

        // Display the total fine to the user
        JOptionPane.showMessageDialog(null, "The book with ID " + bookId + " has been returned.\n\nFine Amount: Rs. " + fineAmount, "Book Return", JOptionPane.INFORMATION_MESSAGE);

        // Update the book status to "Available" in the Books table
        updateBookStatus(bookId, "Available", dbConnection);

        // Remove the book entry from the BorrowedBooks table
        deleteFromBorrowedBooks(bookId, dbConnection);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Oops! An error occurred during book return. Please contact support for assistance.", "Book Return Error", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
    }
}

// Helper method to update the book status in the Books table
private void updateBookStatus(String bookId, String status, Connection dbConnection) throws SQLException {
    PreparedStatement updateStatus = dbConnection.prepareStatement("UPDATE Book SET status = ? WHERE isbn = ?;");
    updateStatus.setString(1, status);
    updateStatus.setString(2, bookId);
    updateStatus.executeUpdate();
}

// Helper method to delete the book entry from the BorrowedBooks table
private void deleteFromBorrowedBooks(String bookId, Connection dbConnection) throws SQLException {
    PreparedStatement deleteBook = dbConnection.prepareStatement("DELETE FROM Borrowed_Books WHERE isbn = ?;");
    deleteBook.setString(1, bookId);
    deleteBook.executeUpdate();
}

    
    void deleteBook(String isbn, Response objResponse, Connection dbConnection) {
        try{
            PreparedStatement p;// (FirstName,LastName,Title) VALUES (?,?,?);");
            p = dbConnection.prepareStatement("delete from book where ISBN=?");
            p.setString(1, isbn);
            int rowsInserted = p.executeUpdate();
            if(rowsInserted > 0){
                objResponse.messagesList.add(new Message("Book deleted successfully.", MessageType.NOTIFICATION));
            }
        }catch(SQLException e){
            objResponse.messagesList.add(new Message(e.getMessage() + "\n Stack Track:\n"+e.getStackTrace(), MessageType.EXCEPTION));
        }
    }

public void borrowBook(String bookISBN, String title, Connection dbConnection) {
    try {
        // Check if the book exists and is available for borrowing
        PreparedStatement checkBook = dbConnection.prepareStatement("SELECT * FROM Book WHERE ISBN = ? AND title = ? AND (status = 'Available' OR status = 'Reserved');");
        checkBook.setString(1, bookISBN);
        checkBook.setString(2, title);
        ResultSet bookResultSet = checkBook.executeQuery();

        if (!bookResultSet.next()) {
            JOptionPane.showMessageDialog(null, "Book with ISBN " + bookISBN + " and title \"" + title + "\" has been borrowed successfully.", "Book Borrowing", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
//        if (!bookResultSet.next()) {
//            JOptionPane.showMessageDialog(null, "Book with ISBN " + bookISBN + " and title \"" + title + "\" is not available for borrowing.", "Book Borrowing", JOptionPane.WARNING_MESSAGE);
//            return;
//        }

        // Set the book status to "Borrowed"
        PreparedStatement setBorrowed = dbConnection.prepareStatement("UPDATE Book SET status = 'Borrowed' WHERE ISBN = ? AND title = ?;");
        setBorrowed.setString(1, bookISBN);
        setBorrowed.setString(2, title);
        int rowsUpdated = setBorrowed.executeUpdate();

        if (rowsUpdated <= 0) {
            JOptionPane.showMessageDialog(null, "Failed to update the book status. Please try again or contact support for assistance.", "Book Borrowing Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a BorrowBookDTO object with current date and return date 20 days from current date
        LocalDate currentDate = LocalDate.now();
        LocalDate returnDate = currentDate.plusDays(20);
        BorrowBookDTO borrowBookDTO = new BorrowBookDTO();
        borrowBookDTO.ISBN = bookISBN;
        borrowBookDTO.name = title;
        borrowBookDTO.currentDate = currentDate;
        borrowBookDTO.returnDate = returnDate;

        // Store the BorrowBookDTO object in the database
        PreparedStatement insertBorrow = dbConnection.prepareStatement("INSERT INTO Borrowed_Books (ISBN, name, currentDate, returnDate) VALUES (?, ?, ?, ?);");
        insertBorrow.setString(1, borrowBookDTO.ISBN);
        insertBorrow.setString(2, borrowBookDTO.name);
        insertBorrow.setDate(3, java.sql.Date.valueOf(borrowBookDTO.currentDate));
        insertBorrow.setDate(4, java.sql.Date.valueOf(borrowBookDTO.returnDate));
        int rowsInserted = insertBorrow.executeUpdate();

        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Book with ISBN " + bookISBN + " and title \"" + title + "\" has been borrowed successfully.", "Book Borrowing", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to borrow the book. Please check the book ISBN and title.", "Book Borrowing Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Oops! An error occurred while borrowing the book. Please contact support for assistance.", "Book Borrowing Error", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
    }
}

     
      
 public void deleteAccount(String username, String email, Connection dbConnection) {
    try {
        PreparedStatement p = dbConnection.prepareStatement("DELETE FROM Users WHERE Username = ? AND email = ?");
        p.setString(1, username);
        p.setString(2, email);
        int rowsDeleted = p.executeUpdate();
        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(null, "Account deleted successfully", "Account Deletion", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "aaFailed to delete the account. Please contact support for assistance", "Account Deletion Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Oops! Failed to delete the account. Please contact support for assistance", "Account Deletion Error", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
    }
}

public void orderBook(OrderDTO order, Connection dbConnection) {
    try {
        // Check if the book exists in the database table 'Books'
        PreparedStatement checkBook = dbConnection.prepareStatement("SELECT * FROM Book WHERE ISBN = ?;");
        checkBook.setString(1, order.getISBN());
        ResultSet bookResultSet = checkBook.executeQuery();

        if (bookResultSet.next()) {
            // Book with the same ISBN already exists, don't order the book
            JOptionPane.showMessageDialog(null, "Book with ISBN " + order.getISBN() + " already exists.", "Order Book", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Insert the book into the database table 'order_book'
        PreparedStatement insertBook = dbConnection.prepareStatement("INSERT INTO book_orders (ISBN, title, author) VALUES (?, ?, ?);");
        insertBook.setString(1, order.getISBN());
        insertBook.setString(2, order.getTitle());
        insertBook.setString(3, order.getAuthor());
        int rowsInserted = insertBook.executeUpdate();

        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Book ordered successfully.", "Order Book", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to order the book. Please contact support for assistance.", "Order Book", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Oops! An error occurred while ordering the book. Please contact support for assistance.", "Order Book", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
    }
}


public void reserveBook(String bookISBN, String Title, Connection dbConnection) {
    try {
        // Check if the book is already reserved
        PreparedStatement checkReserved = dbConnection.prepareStatement("SELECT * FROM Book WHERE ISBN = ? AND title = ? AND status = 'Reserved';");
        checkReserved.setString(1, bookISBN);
        checkReserved.setString(2, Title);
        ResultSet reservedResultSet = checkReserved.executeQuery();

        if (reservedResultSet.next()) {
            JOptionPane.showMessageDialog(null, "Book with ISBN " + bookISBN + " and title \"" + Title + "\" is already reserved.", "Book Reservation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update the status of the book to "Reserved"
        PreparedStatement updateStatus = dbConnection.prepareStatement("UPDATE Book SET status = 'reserved' WHERE ISBN = ? AND title = ?;");
        updateStatus.setString(1, bookISBN);
        updateStatus.setString(2, Title);
        int rowsUpdated = updateStatus.executeUpdate();

        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Book with ISBN " + bookISBN + " and title \"" + Title + "\" has been reserved successfully.", "Book Reservation", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to reserve the book. Please check the book ISBN and title.", "Book Reservation Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Oops! An error occurred while reserving the book. Please contact support for assistance.", "Book Reservation Error", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, e.getMessage() + "\nStack Trace:\n" + e.getStackTrace(), "Exception Details", JOptionPane.ERROR_MESSAGE);
    }
}


public void blockAccount(String email, Response objResponse, Connection dbConnection) {
    try {
        System.out.println("I am blockAccount method in RecordsModifier");
        // Check if the user exists
        PreparedStatement checkUser = dbConnection.prepareStatement("SELECT * FROM Users WHERE email = ?;");
        checkUser.setString(1, email);
        ResultSet userResultSet = checkUser.executeQuery();
        if (userResultSet.next()) {
            // Perform the account blocking logic here
            // ...
            
            // Example: Update the user's account status to blocked
            System.out.println("i am updater");
            PreparedStatement blockUser = dbConnection.prepareStatement("UPDATE Users SET status = 'blocked' WHERE email = ?;");
            blockUser.setString(1, email);
            int rowsUpdated = blockUser.executeUpdate();
            if (rowsUpdated > 0) {
                objResponse.messagesList.add(new Message("Account blocked successfully.", MessageType.NOTIFICATION));
            }
        } else {
            objResponse.messagesList.add(new Message("User with username " + email + " does not exist.", MessageType.WARNING));
        }
    } catch (SQLException e) {
        objResponse.messagesList.add(new Message("Oops! Failed to block the account. Please contact support for assistance.", MessageType.ERROR));
        objResponse.messagesList.add(new Message(e.getMessage() + "\nStack Track:\n" + e.getStackTrace(), MessageType.EXCEPTION));
    }
}

 

    
}
