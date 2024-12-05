package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.CreditCard;

import java.io.Serializable;
import java.time.LocalDate;

public record CreditCardResponseDTO(int id,
                                    int cardNumber,
                                    int cvv,
                                    boolean isActive,
                                    LocalDate expiryDate,
                                    AddressResponseDTO billingAddress)

        implements Serializable {


    public CreditCardResponseDTO(CreditCard creditCard) {
        this(creditCard.getId(), creditCard.getCardNumber(), creditCard.getCvv(), creditCard.getActive(), creditCard.getExpiryDate(), new AddressResponseDTO(creditCard.getBillingAddress()));

    }

}