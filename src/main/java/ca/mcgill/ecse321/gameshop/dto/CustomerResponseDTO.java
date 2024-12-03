package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Customer;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record CustomerResponseDTO(String username,
                                  String password,
                                  String email,
                                  String phoneNumber,
                                  Set<AddressResponseDTO> addresses,
                                  Set<CreditCardResponseDTO> creditCards,
                                  Set<ReviewDTO> likedReviews,
                                  int id) implements Serializable {


    public CustomerResponseDTO(Customer customer) {
        this(customer.getUsername(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getPhoneNumber()
                ,customer.getCopyAddresses().stream().map(AddressResponseDTO::new).collect(Collectors.toSet())
                ,customer.getCopyofCreditCards().stream().map(CreditCardResponseDTO::new).collect(Collectors.toSet())
                ,customer.getCopyLikedReviews().stream().map(ReviewDTO::new).collect(Collectors.toSet())
                , customer.getId());
    }

    public Customer toCustomer() {
        return new Customer(this.username, this.password, this.email, this.phoneNumber);
    }
}

