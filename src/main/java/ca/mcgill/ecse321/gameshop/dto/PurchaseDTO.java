package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Purchase;

import java.io.Serializable;
import java.time.LocalDate;

public record PurchaseDTO (int id, LocalDate purchaseDate, float purchasePrice, GameResponseDTO game, CustomerResponseDTO customer, AddressResponseDTO billingAddress, CreditCardResponseDTO creditCard, ReviewDTO review, RefundRequestDTO refundRequest) implements Serializable {


    public PurchaseDTO(Purchase purchase) {
        this(purchase.getId(),
                purchase.getPurchaseDate(),
                purchase.getPurchasePrice(),
                new GameResponseDTO(purchase.getGamePurchased()),
                new CustomerResponseDTO(purchase.getCustomer()),
                new AddressResponseDTO(purchase.getDeliveryAddress()),
                new CreditCardResponseDTO(purchase.getPaymentMethod()),
                purchase.getReview() != null ? new ReviewDTO(purchase.getReview()) : null,
                purchase.getRefundRequest() != null ? new RefundRequestDTO(purchase.getRefundRequest()) : null
                );
    }
}