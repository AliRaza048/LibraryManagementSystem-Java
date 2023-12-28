
package model.dto;

import java.util.ArrayList;
import java.util.List;


public class Response {
    public List<Message> messagesList;
    private boolean success;


    public Response() {
        this.messagesList = new ArrayList<>();
         this.success = true;
    }

   public boolean hasError() {
      for(Message m : messagesList)
      {
         if(m.Type == MessageType.ERROR || m.Type == MessageType.EXCEPTION)
             return true;
     }
    return false;
   }
//
 public String getErrorMessages() {
     StringBuilder sb = new StringBuilder();    
     for(Message m : messagesList)
        {
           if(sb.length() > 0) 
               sb.append(",\n");
            if(m.Type == MessageType.ERROR || m.Type == MessageType.EXCEPTION)
                sb.append(m.Message);
        }
        return sb.toString();
    }

    public boolean isSuccessful() {
        return !hasError();
    }

   public List<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Message> messagesList) {
        this.messagesList = messagesList;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
}
