package ca.mcgill.ecse321.gameshop.controller;


import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.CreditCard;
import ca.mcgill.ecse321.gameshop.model.Purchase;
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


//    @GetMapping("games/{gameId}")
//    @ResponseStatus(HttpStatus.FOUND)
//    public GameDTO getGameById(@PathVariable int gameId) {
//        return new GameDTO(purchaseManagementService.findGameById(gameId));
//    }

    @GetMapping("/games/{gameId}/price")
    @ResponseStatus(HttpStatus.FOUND)
    public float getPromotionalPrice(@PathVariable int gameId) {
        return purchaseManagementService.getPromotionalPrice(gameId);
    }

    @GetMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.FOUND)
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
    @ResponseStatus(HttpStatus.FOUND)
    public CustomerDTO getCustomerByEmail(@PathVariable String customerEmail) {
        return new CustomerDTO(purchaseManagementService.findCustomerByEmail(customerEmail));
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
    @ResponseStatus(HttpStatus.FOUND)
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
    @ResponseStatus(HttpStatus.FOUND)
    public AddressDTO getAddressById(@PathVariable int addressId) {
        return new AddressDTO(purchaseManagementService.findAddressById(addressId));
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
    @ResponseStatus(HttpStatus.FOUND)
    public CreditCardDTO getCreditCardById(@PathVariable int creditCardId) {
        return new CreditCardDTO(purchaseManagementService.findCreditCardById(creditCardId));
    }

    @PostMapping("customers/{customerEmail}/credit-cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardDTO addCreditCardToCustomerWallet(@RequestBody CreditCardDTO creditCardDTO,
                                                       @RequestParam String expiryDate,
                                                       @RequestParam int addressId,
                                                       @PathVariable String customerEmail) {
        return new CreditCardDTO(purchaseManagementService.addCreditCardToCustomerWallet(creditCardDTO.cardNumber(),creditCardDTO.cvv(),expiryDate,customerEmail,addressId));

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
    @ResponseStatus(HttpStatus.FOUND)
    public Set<CreditCardDTO> getCreditCardsByCustomer(@PathVariable String customerEmail) {
        Set<CreditCard> wallet = purchaseManagementService.viewCustomerCreditCards(customerEmail);
        return wallet.stream().map(CreditCardDTO::new).collect(Collectors.toSet());
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
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.FOUND)
    public float getCartPrice(@PathVariable String customerEmail) {
        return purchaseManagementService.getCartPrice(customerEmail);
    }

    @GetMapping("refunds/{refundId}")
    @ResponseStatus(HttpStatus.FOUND)
    public RefundRequestDTO getRefundById(@PathVariable int refundId) {
        return new RefundRequestDTO(purchaseManagementService.findRefundById(refundId));
    }

    @GetMapping("employees/{employeeUsername}")
    @ResponseStatus(HttpStatus.FOUND)
    public EmployeeDTO getEmployeeByUsername(@PathVariable String employeeUsername) {
        return new EmployeeDTO(purchaseManagementService.findEmployeeByUsername(employeeUsername));
    }
    
    @GetMapping("customers/{customerEmail}/purchaseHistory")
    @ResponseStatus(HttpStatus.FOUND)
    public Set<PurchaseDTO> requestCustomerPurchaseHistory(@PathVariable String customerEmail, @RequestParam String requestor) {
        Set<Purchase> purchases;
        if (!requestor.isEmpty()) {
            purchases = purchaseManagementService.requestCustomersPurchaseHistory(customerEmail, requestor);
        }
        else {
            purchases = purchaseManagementService.viewCustomerPurchaseHistory(customerEmail);
        }
            return purchases.stream().map(PurchaseDTO::new).collect(Collectors.toSet());
    }

    @PostMapping("refunds")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RefundRequestDTO requestRefund(@RequestParam int purchaseId, @RequestParam String reason) {
        return new RefundRequestDTO(purchaseManagementService.requestRefund(purchaseId, reason));
    }

    @PutMapping("refunds/{refundId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void processRefund(@PathVariable int refundId, @RequestParam String employeeUsername, @RequestParam boolean approve) {
        if (approve) { // Put is to approve, not deny
            purchaseManagementService.approveRefund(refundId, employeeUsername);
        }
        else { // approve = false, therefore deny, not approve
            purchaseManagementService.denyRefund(refundId, employeeUsername);
        }
    }

    @PutMapping("refunds/{refundId}/reviewer")
    @ResponseStatus(HttpStatus.ACCEPTED) 
    public void updateRefundReviewer(@PathVariable int refundId, @RequestParam String reviewerUsername, @RequestParam boolean add) {
        if (add) { // Adding reviewer, not removing
            purchaseManagementService.addReviewerToRefundRequest(refundId, reviewerUsername);
        }
        else { // Removing reviewer, not adding
            purchaseManagementService.removeReviewerFromRefundRequest(refundId, reviewerUsername);
        }
    }
}
