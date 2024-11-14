package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.CreditCard;

import java.io.Serializable;
import java.time.LocalDate;

public record CreditCardRequestDTO(int cardNumber,
                                    int cvv,
                                    LocalDate expiryDate,
                                    AddressResponseDTO billingAddress)
        implements Serializable {


    public CreditCardRequestDTO(CreditCard creditCard) {
        this(creditCard.getCardNumber(), creditCard.getCvv(),  creditCard.getExpiryDate(), new AddressResponseDTO(creditCard.getBillingAddress()));

    }

}