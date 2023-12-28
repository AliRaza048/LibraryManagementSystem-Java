/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String status;
    private String gender;

    public UserDTO(String Email,String username, String password ,String status,String gender) {
        this.email=Email;
        this.username = username;
        this.password = password;
        this.status=status;
        this.gender=gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String username) {
        this.gender = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String username) {
        this.email = username;
    }
}

