/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.dto.BookDTO;

public class RecordsMapper {

 

    ArrayList<BookDTO> getBooks(ResultSet rs) {
        ArrayList<BookDTO> emplist = new ArrayList<>();
        try{
        while (rs.next())
            {
                BookDTO objBook=new BookDTO();                
           objBook.ISBN  =rs.getString(1);
               objBook.authorname= rs.getString(3);
               objBook.category =rs.getString(2);
              objBook.name=rs.getString(8);  
              objBook.publishername=rs.getString(5);
                emplist.add(objBook);
            }
        }catch (Exception e){
        }
        return emplist;
    }
    
}
