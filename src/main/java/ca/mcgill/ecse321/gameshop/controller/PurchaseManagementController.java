package ca.mcgill.ecse321.gameshop.controller;


import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.CreditCard;
import ca.mcgill.ecse321.gameshop.serviceClasses.PurchaseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PurchaseManagementController {

    @Autowired
    PurchaseManagementService purchaseManagementService;


    @GetMapping("games/{gameId}")
    public GameDTO getGameById(@PathVariable int gameId) {
        return new GameDTO(purchaseManagementService.findGameById(gameId));
    }

    @GetMapping("reviews/{reviewId}")
    public ReviewDTO getReviewById(@PathVariable int reviewId) {
        return new ReviewDTO(purchaseManagementService.findReviewById(reviewId));
    }

    @GetMapping("customers/{customerEmail}")
    public CustomerDTO getReviewById(@PathVariable String customerEmail) {
        return new CustomerDTO(purchaseManagementService.findCustomerByEmail(customerEmail));
    }

    @GetMapping("purchases/{purchaseId}")
    public PurchaseDTO getPurchaseById(@PathVariable int purchaseId) {
        return new PurchaseDTO(purchaseManagementService.findPurchaseById(purchaseId));
    }

    @GetMapping("creditcards/{creditCardId}")
    public CreditCardDTO getCreditCardById(@PathVariable int creditCardId) {
        return new CreditCardDTO(purchaseManagementService.findCreditCardById(creditCardId));
    }

    @GetMapping("addresses/{addressId}")
    public AddressDTO getAddressById(@PathVariable int addressId) {
        return new AddressDTO(purchaseManagementService.findAddressById(addressId));
    }

    @PostMapping("{customerEmail}/creditCards")
    public CreditCardDTO createCreditCard(@RequestBody int cardNumber,
                                          @RequestBody String cvv,
                                          @PathVariable String customerEmail,
                                          @RequestBody String expiryDate,
                                          @RequestBody int addressId) {
        return new CreditCardDTO(purchaseManagementService.createCreditCard(cardNumber,cvv,expiryDate,customerEmail,addressId));

    }

    @PutMapping("reviews/{reviewId}/{customerEmail}")
    public void likeReview(@PathVariable int reviewId, @PathVariable String customerEmail) {
        purchaseManagementService.likeReview(customerEmail, reviewId);
    }

    @PostMapping("{customerEmail}/reviews/{reviewId}/{rating}")
    public void postReview(@PathVariable String customerEmail, @PathVariable int reviewId, @PathVariable int rating, @RequestBody String text) {
        purchaseManagementService.postReview(customerEmail,rating,text,reviewId);
    }

    @PostMapping("reviews/{reviewId}/{managerId}")
    public void replyToReview(@PathVariable int reviewId, @PathVariable int managerId, @RequestBody String replyText) {
        purchaseManagementService.replyToReview(reviewId, replyText,managerId);
    }

    @PostMapping("{customerEmail}/cart")
    public void chechout(@PathVariable String customerEmail, @RequestBody int billingAddressId, @RequestBody int creditCardId) {
        purchaseManagementService.checkout(customerEmail, billingAddressId, creditCardId);
    }

    @GetMapping("{customerEmail}/cart")
    public float getCartPrice(@PathVariable String customerEmail) {
        return purchaseManagementService.getCartPrice(customerEmail);
    }

    @GetMapping("games/{gameId}")
    public float getPromotionalPrice(@PathVariable int gameId) {
        return purchaseManagementService.getPromotionalPrice(gameId);
    }

    @GetMapping("customers/{customerEmail}/creditCards")
    public Set<CreditCardDTO> getCreditCards(@PathVariable String customerEmail) {
     Set<CreditCard> wallet = purchaseManagementService.viewCustomerCreditCards(customerEmail);
     return wallet.stream().map(CreditCardDTO::new).collect(Collectors.toSet());
    }

    @PutMapping("{customerEmail}/creditCards/{creditCardId}")
    public void addCreditCardToWallet(@PathVariable String customerEmail, @PathVariable int creditCardId) {
        purchaseManagementService.addCreditCardToCustomerWallet(customerEmail, creditCardId);
    }

    @DeleteMapping("{customerEmail}/creditCards/{creditCardId}")
    public void removeCreditCardFromWallet(@PathVariable String customerEmail, @PathVariable int creditCardId) {
        purchaseManagementService.removeCreditCardFromWallet(customerEmail, creditCardId);
    }

}
