package ca.mcgill.ecse321.gameshop.dto;

public class AccountRequestDTO {
    private String email;
    private String password;
    private String username;
    private String phoneNumber;

    public AccountRequestDTO(String email, String password, String username, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String username) {
        this.username = username;
    }

    public void setImageURL(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}