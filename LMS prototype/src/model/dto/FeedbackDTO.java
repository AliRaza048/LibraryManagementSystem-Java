package model.dto;

public class FeedbackDTO {
    private String kind;
    private String comments;
    private String email;
    private String phoneNumber;
    private String about;

    public FeedbackDTO(String kind, String comments, String email, String phoneNumber, String about) {
        this.kind = kind;
        this.comments = comments;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.about = about;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
