package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Purchase;

import java.io.Serializable;
import java.time.LocalDate;

public record PurchaseDTO (int id, LocalDate purchaseDate, float purchasePrice, GameResponseDTO game, CustomerDTO customer, AddressDTO billingAddress, CreditCardDTO creditCard, ReviewDTO review, RefundRequestDTO refundRequest) implements Serializable {


    public PurchaseDTO(Purchase purchase) {
        this(purchase.getId(),
                purchase.getPurchaseDate(),
                purchase.getPurchasePrice(),
                new GameResponseDTO(purchase.getGamePurchased()),
                new CustomerDTO(purchase.getCustomer()),
                new AddressDTO(purchase.getDeliveryAddress()),
                new CreditCardDTO(purchase.getPaymentMethod()),
                new ReviewDTO(purchase.getReview()),
                new RefundRequestDTO(purchase.getRefundRequest()));
    }
}