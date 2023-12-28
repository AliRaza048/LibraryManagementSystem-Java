/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.util.List;

public class FeedbackResponse {
    private boolean success;
    private String errorMessage;
    private MessageType messageType;

    public FeedbackResponse() {
        this.success = true;
        this.errorMessage = "";
        this.messageType = MessageType.NOTIFICATION;
    }
     public List<Message> messagesList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    
      public List<Message> getMessagesList() {
        return messagesList;
    }
}

