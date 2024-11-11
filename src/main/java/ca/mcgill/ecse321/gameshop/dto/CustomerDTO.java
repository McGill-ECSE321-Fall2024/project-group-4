package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Customer;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record CustomerDTO(String username,
                          String password,
                          String email,
                          String phoneNumber,
                          Set<GameDTO> cart,
                          Set<AddressDTO> addresses,
                          Set<CreditCardDTO> creditCards,
                          Set<ReviewDTO> likedReviews,
                          Set<PurchaseDTO> purchases) implements Serializable {


    public CustomerDTO(Customer customer) {
        this(customer.getUsername(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getPhoneNumber()
        ,customer.getCopyCart().stream().map(GameDTO::new).collect(Collectors.toSet())
        ,customer.getCopyAddresses().stream().map(AddressDTO::new).collect(Collectors.toSet())
        ,customer.getCopyofCreditCards().stream().map(CreditCardDTO::new).collect(Collectors.toSet())
        ,customer.getCopyLikedReviews().stream().map(ReviewDTO::new).collect(Collectors.toSet())
                ,customer.getCopyPurchases().stream().map(PurchaseDTO::new).collect(Collectors.toSet()));
    }

}
