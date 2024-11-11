package ca.mcgill.ecse321.gameshop.controller;


import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.CreditCard;
import ca.mcgill.ecse321.gameshop.serviceClasses.PurchaseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PurchaseManagementController {

    @Autowired
    PurchaseManagementService purchaseManagementService;


    @GetMapping("games/{gameId}")
    @ResponseStatus(HttpStatus.FOUND)
    public GameDTO getGameById(@PathVariable int gameId) {
        return new GameDTO(purchaseManagementService.findGameById(gameId));
    }

    @GetMapping("games/promotions/{gameId}")
    @ResponseStatus(HttpStatus.FOUND)
    public float getPromotionalPrice(@PathVariable int gameId) {
        return purchaseManagementService.getPromotionalPrice(gameId);
    }

    @GetMapping("reviews/{reviewId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ReviewDTO getReviewById(@PathVariable int reviewId) {
        return new ReviewDTO(purchaseManagementService.findReviewById(reviewId));
    }

    @PutMapping("customers/{customerEmail}/reviews/{reviewId}/likes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void likeReview(@PathVariable int reviewId, @PathVariable String customerEmail) {
        purchaseManagementService.likeReview(customerEmail, reviewId);
    }

    @PostMapping("customers/{customerEmail}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public void postReview(@PathVariable String customerEmail, @RequestParam int purchaseId, @RequestParam int rating, @RequestBody String text) {
        purchaseManagementService.postReview(customerEmail,rating,text, purchaseId);
    }

    @PostMapping("reviews/{reviewId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public void replyToReview(@PathVariable int reviewId, @RequestParam int managerId, @RequestBody String replyText) {
        purchaseManagementService.replyToReview(reviewId, replyText,managerId);
    }

    @GetMapping("customers/{customerEmail}")
    @ResponseStatus(HttpStatus.FOUND)
    public CustomerDTO getCustomerByEmail(@PathVariable String customerEmail) {
        return new CustomerDTO(purchaseManagementService.findCustomerByEmail(customerEmail));
    }

    @GetMapping("purchases/{purchaseId}")
    @ResponseStatus(HttpStatus.FOUND)
    public PurchaseDTO getPurchaseById(@PathVariable int purchaseId) {
        return new PurchaseDTO(purchaseManagementService.findPurchaseById(purchaseId));
    }

    @GetMapping("addresses/{addressId}")
    @ResponseStatus(HttpStatus.FOUND)
    public AddressDTO getAddressById(@PathVariable int addressId) {
        return new AddressDTO(purchaseManagementService.findAddressById(addressId));
    }

    @GetMapping("credit-cards/{creditCardId}")
    @ResponseStatus(HttpStatus.FOUND)
    public CreditCardDTO getCreditCardById(@PathVariable int creditCardId) {
        return new CreditCardDTO(purchaseManagementService.findCreditCardById(creditCardId));
    }

    @PutMapping("customers/{customerEmail}/credit-cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardDTO addCreditCardToCustomerWallet(@RequestBody int cardNumber,
                                                       @RequestBody String cvv,
                                                       @PathVariable String customerEmail,
                                                       @RequestBody String expiryDate,
                                                       @RequestBody int addressId) {
        return new CreditCardDTO(purchaseManagementService.addCreditCardToCustomerWallet(cardNumber,cvv,expiryDate,customerEmail,addressId));

    }

    @GetMapping("customers/{customerEmail}/credit-cards")
    @ResponseStatus(HttpStatus.FOUND)
    public Set<CreditCardDTO> getCreditCardsByCustomer(@PathVariable String customerEmail) {
        Set<CreditCard> wallet = purchaseManagementService.viewCustomerCreditCards(customerEmail);
        return wallet.stream().map(CreditCardDTO::new).collect(Collectors.toSet());
    }

    @DeleteMapping("customers/{customerEmail}/credit-cards/{creditCardId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeCreditCardFromWallet(@PathVariable String customerEmail, @PathVariable int creditCardId) {
        purchaseManagementService.removeCreditCardFromWallet(customerEmail, creditCardId);
    }

    @PostMapping("customers/{customerEmail}/cart")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void checkout(@PathVariable String customerEmail, @RequestBody int billingAddressId, @RequestBody int creditCardId) {
        purchaseManagementService.checkout(customerEmail, billingAddressId, creditCardId);
    }

    @GetMapping("customers/{customerEmail}/cart/price")
    @ResponseStatus(HttpStatus.FOUND)
    public float getCartPrice(@PathVariable String customerEmail) {
        return purchaseManagementService.getCartPrice(customerEmail);
    }

    @GetMapping("refunds/{refundId}")
    @ResponseStatus(HttpStatus.FOUND)
    public RefundRequestDTO getRefundById(@PathVariable int refundId) {
        return new RefundRequestDTO(purchaseManagementService.getRefundById(refundId));
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
            purchases = purchaseManagementService.requestCustomerPurchaseHistory(customerEmail, requestor);
        }
        else {
            purchases = purchaseManagementService.viewCustomerPurchaseHistory(customerEmail);
        }
            return purchases.stream().map(PurchaseDTO::new).Collect(Collectors.toSet());    
    }

    @PostMapping("refunds")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RefundRequestDTO requestRefund(@RequestParam int purchaseId, @RequestParam String reason) {
        return new RefundRequestDTO(purchaseManagementService.requestRefund(purchaseId, reason));
    }
}
