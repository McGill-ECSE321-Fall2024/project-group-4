package ca.mcgill.ecse321.gameshop.controller;


import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.CreditCard;
import ca.mcgill.ecse321.gameshop.serviceClasses.PurchaseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Methods for PurchaseManagementController
 *
 * @author
 */
@RestController
public class PurchaseManagementController {

    @Autowired
    PurchaseManagementService purchaseManagementService;


    @GetMapping("/games/{gameId}/price")
    public float getPromotionalPrice(@PathVariable int gameId) {
        return purchaseManagementService.getPromotionalPrice(gameId);
    }

    @GetMapping("/reviews/{reviewId}")
    public ReviewDTO getReviewById(@PathVariable int reviewId) {
        return new ReviewDTO(purchaseManagementService.findReviewById(reviewId));
    }

    /**
     * Like a review
     *
     * @param reviewId Review unique identifier
     * @param customerEmail Email of the customer liking the review
     *
     * @author
     */
    @PutMapping("/customers/{customerEmail}/reviews/{reviewId}/likes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void likeReview(@PathVariable int reviewId, @PathVariable String customerEmail) {
        purchaseManagementService.likeReview(customerEmail, reviewId);
    }

    /**
     * Post a review
     *
     * @param customerEmail Customer unique email of the poster
     * @param purchaseId Purchase unique identifier
     * @param rating rating out of 5 of purchase
     * @param text text of the review
     *
     * @author
     */
    @PostMapping("/customers/{customerEmail}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public void postReview(@PathVariable String customerEmail, @RequestParam int purchaseId, @RequestParam int rating, @RequestBody String text) {
        purchaseManagementService.postReview(customerEmail,rating,text, purchaseId);
    }

    /**
     * Reply to a review
     *
     * @param reviewId Review unique identifier
     * @param managerId Manager unique identifier
     * @param replyText Text of the reply
     *
     * @author
     */
    @PostMapping("/reviews/{reviewId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public void replyToReview(@PathVariable int reviewId, @RequestParam int managerId, @RequestBody String replyText) {
        purchaseManagementService.replyToReview(reviewId, replyText,managerId);
    }

    /**
     * Get a customer by their unique email
     *
     * @param customerEmail Customer unique email
     * @return Customer DTO with corresponding to the email
     *
     * @author
     */
    @GetMapping("customers/{customerEmail}")
    public CustomerResponseDTO getCustomerByEmail(@PathVariable String customerEmail) {
        return new CustomerResponseDTO(purchaseManagementService.findCustomerByEmail(customerEmail));
    }

    /**
     * Get purchase from id
     *
     * @param purchaseId Purchase unique identifier
     * @return Purchase DTO with corresponding id
     *
     * @author
     */
    @GetMapping("purchases/{purchaseId}")
    public PurchaseDTO getPurchaseById(@PathVariable int purchaseId) {
        return new PurchaseDTO(purchaseManagementService.findPurchaseById(purchaseId));
    }

    /**
     * Get address from id
     *
     * @param addressId address unique identifier
     * @return Address DTO from corresponding id
     *
     * @author
     */
    @GetMapping("addresses/{addressId}")
    public AddressResponseDTO getAddressById(@PathVariable int addressId) {
        return new AddressResponseDTO(purchaseManagementService.findAddressById(addressId));
    }

    /**
     * Get credit card from id
     *
     * @param creditCardId Credit card unique identifier
     * @return Credit Card DTO with corresponding id
     *
     * @author
     */
    @GetMapping("credit-cards/{creditCardId}")
    public CreditCardResponseDTO getCreditCardById(@PathVariable int creditCardId) {
        return new CreditCardResponseDTO(purchaseManagementService.findCreditCardById(creditCardId));
    }

    @PostMapping("customers/{customerEmail}/credit-cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardResponseDTO addCreditCardToCustomerWallet(@RequestBody CreditCardRequestDTO creditCardRequestDTO, @PathVariable String customerEmail) {

        return new CreditCardResponseDTO(purchaseManagementService.addCreditCardToCustomerWallet(creditCardRequestDTO.cardNumber(),
                creditCardRequestDTO.cvv(), String.valueOf(creditCardRequestDTO.expiryDate()),customerEmail, creditCardRequestDTO.billingAddress().id()));
    }

    /**
     * Get a customer credit cards
     *
     * @param customerEmail Customer unique email
     * @return List of credit cards from the customer
     *
     * @author
     */
    @GetMapping("customers/{customerEmail}/credit-cards")
    public Set<CreditCardResponseDTO> getCreditCardsByCustomer(@PathVariable String customerEmail) {
        Set<CreditCard> wallet = purchaseManagementService.viewCustomerCreditCards(customerEmail);
        return wallet.stream().map(CreditCardResponseDTO::new).collect(Collectors.toSet());
    }

    /**
     * Remove a credit card from a customer wallet
     *
     * @param customerEmail Customer unique email
     * @param creditCardId Credit card unique identifier from customer wallet
     *
     * @author
     */
    @DeleteMapping("customers/{customerEmail}/credit-cards/{creditCardId}")
    public void removeCreditCardFromWallet(@PathVariable String customerEmail, @PathVariable int creditCardId) {
        purchaseManagementService.removeCreditCardFromWallet(customerEmail, creditCardId);
    }

    @PostMapping("customers/{customerEmail}/cart")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void checkout(@PathVariable String customerEmail, @RequestParam int billingAddressId, @RequestParam int creditCardId) {
        purchaseManagementService.checkout(customerEmail, billingAddressId, creditCardId);
    }

    /**
     * Get cart price of a customer
     *
     * @param customerEmail Customer unique email
     * @return Price of the cart
     *
     * @author
     */
    @GetMapping("customers/{customerEmail}/cart/price")
    public float getCartPrice(@PathVariable String customerEmail) {
        return purchaseManagementService.getCartPrice(customerEmail);
    }

}
