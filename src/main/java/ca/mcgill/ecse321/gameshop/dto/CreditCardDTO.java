package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.CreditCard;

import java.io.Serializable;
import java.time.LocalDate;

public record CreditCardDTO(int id,
                            int cardNumber,
                            String cvv,
                            LocalDate expiryDate,
                            CustomerDTO customer)
        implements Serializable {


    public CreditCardDTO(CreditCard creditCard) {
        this(creditCard.getId(), creditCard.getCardNumber(), creditCard.getCvv(),  creditCard.getExpiryDate(),new CustomerDTO(creditCard.getCustomer()));

    }

}