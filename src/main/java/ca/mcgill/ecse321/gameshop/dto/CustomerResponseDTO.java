package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Account;
import ca.mcgill.ecse321.gameshop.model.Customer;

public class CustomerResponseDTO {
    private Integer id;
    private String email;
    private String password;
    private String username;
    private String phoneNumber;

    private String error;

    public CustomerResponseDTO() {  
    }

    public CustomerResponseDTO(String error) { 
        this.error = error;
    }

    public CustomerResponseDTO(Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.password = customer.getPassword();
        this.username = customer.getUsername();
        this.phoneNumber = customer.getPhoneNumber();
        this.error = "";
    }

    public static CustomerResponseDTO create(Account account) {
        if (account instanceof Customer) {
            return new CustomerResponseDTO((Customer) account);
        } else {
            throw new IllegalArgumentException("Unknown account type.");
        }
    }

    public Integer getId() {
        return id;
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

    public String getError() {
        return error;
    }

    public String getPhoneNumber()
    {
      return phoneNumber;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setError(String error) {
        this.error = error;
    }
}