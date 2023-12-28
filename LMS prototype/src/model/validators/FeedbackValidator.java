package model.validators;
import model.dto.FeedbackDTO;
import model.dto.Message;
import model.dto.MessageType;
import model.dto.Response;
import model.dto.FeedbackResponse;

public class FeedbackValidator {
    public void validateFeedback(FeedbackDTO feedbackDTO, FeedbackResponse response) {
        if (feedbackDTO == null) {
            response.setSuccess(false);
            response.getMessagesList().add(new Message("Invalid feedback entry.", MessageType.ERROR));
        } else {
            if (feedbackDTO.getKind() == null || feedbackDTO.getKind().isEmpty()) {
                response.setSuccess(false);
                response.getMessagesList().add(new Message("Kind is required.", MessageType.WARNING));
            }
            if (feedbackDTO.getComments() == null || feedbackDTO.getComments().isEmpty()) {
                response.setSuccess(false);
                response.getMessagesList().add(new Message("Comments are required.", MessageType.WARNING));
            }
            if (feedbackDTO.getEmail() == null || feedbackDTO.getEmail().isEmpty()) {
                response.setSuccess(false);
                response.getMessagesList().add(new Message("Email is required.", MessageType.WARNING));
            }
            if (feedbackDTO.getPhoneNumber() == null || feedbackDTO.getPhoneNumber().isEmpty()) {
                response.setSuccess(false);
                response.getMessagesList().add(new Message("Phone number is required.", MessageType.WARNING));
            }
            if (feedbackDTO.getAbout() == null || feedbackDTO.getAbout().isEmpty()) {
                response.setSuccess(false);
                response.getMessagesList().add(new Message("About is required.", MessageType.WARNING));
            }
        }
    }
}





