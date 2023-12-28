/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package model.validators;

import model.dto.PaymentDTO;
import model.dto.Response;
import model.dto.Message;
import model.dto.MessageType;

public class PaymentValidator {

    public void validatePayment(PaymentDTO payment, Response response) {
        if (payment == null) {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("Payment object is null.", MessageType.ERROR));
            return;
        }

        if (payment.getPaymentId() == null || payment.getPaymentId().isEmpty()) {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("Payment ID is required.", MessageType.ERROR));
        }

        if (payment.getUserId() == null || payment.getUserId().isEmpty()) {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("User ID is required.", MessageType.ERROR));
        }

        if (payment.getAmount() <= 0) {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("Payment amount should be greater than zero.", MessageType.ERROR));
        }

        // Add additional validation rules as per your requirements
    }
}

