/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.validators;

import model.dto.BookDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;

public class CommonValidator {

    public static void validateBook(BookDTO objBook, Response objResponse) {
        isValidName(objBook.authorname, objResponse);
        isValidName(objBook.category, objResponse);
         isValidName(objBook.name, objResponse);
          isValidName(objBook.publishername, objResponse);
           isValidName(objBook.ISBN, objResponse);
        
    }

   private static void isValidName(String nameToValidate, Response objResponse) {
    if(nameToValidate == null || nameToValidate.length() < 3){
            objResponse.messagesList.add(new Message("FIrst Name is not valid, Provide valid first name with at least 3 characters.",MessageType.ERROR));
        }
}

    
}
