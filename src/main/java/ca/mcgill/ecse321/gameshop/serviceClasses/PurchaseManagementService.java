package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Methods for PurchaseManagementService
 *
 * @author
 */
@Service
public class PurchaseManagementService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RefundRequestRepository refundRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    /*
     * Finds refund request given id
     * @param id the id in question
     * @return RefundRequest with given id
     * @author Aerin Brown
     */
    @Transactional 
    public RefundRequest findRefundById(int id) {
        Optional<RefundRequest> optRefund = refundRepository.findById(id);

        if (optRefund.isPresent()) {
            return optRefund.get();
        }
        throw new EntityNotFoundException("No Refund Request found with id " + id);
    }
    /*
     * Finds employee given a username
     * @param username the employee's username
     * @return Employee with given username
     * @author Aerin Brown
     */
    @Transactional
    public Employee findEmployeeByUsername(String username) {
        if (username == null) throw new EntityNotFoundException("Employee username is null!");

        Optional<Employee> optEmployee = employeeRepository.findByUsername(username);
        if (optEmployee.isPresent()) {
            return optEmployee.get();
        }
        throw new EntityNotFoundException("No Employee found with username " + username);
    }

    /**
     * @param gameId game id of the game to find
     * @return The game with the associated id
     * @throws EntityNotFoundException when no game is found
     * @author Tarek Namani
     */
    @Transactional
    public Game findGameById(int gameId) {
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            return optGame.get();
        }
        throw new EntityNotFoundException("No Game found with id " + gameId);
    }

    /**
     * @param reviewId id of the review to find
     * @return The review with the associated id
     * @throws EntityNotFoundException when no review is found
     * @author Tarek Namani
     */
    @Transactional
    public Review findReviewById(int reviewId) {
        Optional<Review> optReview = reviewRepository.findById(reviewId);
        if (optReview.isPresent()) {
            return optReview.get();
        }
        throw new EntityNotFoundException("No Review found with id " + reviewId);
    }
    /**
     * @param email of the customer to find
     * @return The customer with the associated email
     * @throws EntityNotFoundException when no customer is found
     * @author Tarek Namani
     */
    @Transactional
    public Customer findCustomerByEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email is null!");

        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        if (optCustomer.isPresent()) {
            return optCustomer.get();
        }
        throw new EntityNotFoundException("No Customer found with email " + email);

    }


    /**
     * @param managerId of the manager account
     * @return The manager if found
     * @throws EntityNotFoundException when no manager is found
     * @author Tarek Namani
     */
    @Transactional
    public Manager findManagerById(int managerId) {
        Optional<Manager> optManager = managerRepository.findById(managerId);
        if (optManager.isPresent()) {
            return optManager.get();
        }
        throw new EntityNotFoundException("No manager found with id " + managerId);

    }

    /**
     * @param purchaseId of the purchase
     * @return The purchase if found
     * @throws EntityNotFoundException when no purchase is found
     * @author Tarek Namani
     */
    @Transactional
    public Purchase findPurchaseById(int purchaseId) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(purchaseId);
        if (optPurchase.isPresent()) {
            return optPurchase.get();
        }
        throw new EntityNotFoundException("No Purchase found with id " + purchaseId);
    }

    /**
     * @param creditCardId of the credit card
     * @return The credit card if found
     * @throws EntityNotFoundException when no credit card is found
     * @author Tarek Namani
     */
    @Transactional
    public CreditCard findCreditCardById(int creditCardId) {
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCardId);
        if (optionalCreditCard.isPresent()) {
            return optionalCreditCard.get();
        }
        throw new EntityNotFoundException("No Credit Card found with id " + creditCardId);
    }

    /**
     * @param addressId of the address
     * @return The address if found
     * @throws EntityNotFoundException when no address is found
     * @author Tarek Namani
     */
    @Transactional
    public Address findAddressById(int addressId) {
        Optional<Address> optAdress = addressRepository.findById(addressId);
        if (optAdress.isPresent()) {
            return optAdress.get();
        }
        throw new EntityNotFoundException("No Address found with id " + addressId);
    }


    /**
     * Creates a new credit card with input parameters and adds it to a customers wallet
     *
     * @param cardNumber credit card number
     * @param cvv code of credit card
     * @param expiryDate expiry date of credit card
     * @param customerEmail email of credit card's owner
     * @param addressId billing address of credit card
     * @return created credit card
     * @throws IllegalArgumentException when expiry date and cvv are in the wrong format
     * @throws EntityNotFoundException when customer does not exist, or when the address does not exist
     * @author Tarek Namani
     */
    @Transactional
    public CreditCard addCreditCardToCustomerWallet(int cardNumber, int cvv, String expiryDate, String customerEmail, int addressId) {

        String stringCVV = String.valueOf(cvv); //
        Matcher cvvMatcher = Pattern.compile("^\\d{3}$").matcher(stringCVV); //create a Regex to identify and match valid CVV patterns
        if (!cvvMatcher.matches()) {
            throw new IllegalArgumentException("Invalid cvv number, enter a 3 digit CVV");
        }

        Matcher expiryDateMatcher = Pattern.compile("^(0[1-9]|1[0-2])/(\\d{2})$").matcher(expiryDate); //create a Regex to identify and match a valid Expiration date format
        if (!expiryDateMatcher.matches()) {
            throw new IllegalArgumentException("Invalid expiry date, format is MM/YY");
        }

        LocalDate date = LocalDate.of(2000 + Integer.parseInt(expiryDateMatcher.group(2)), Integer.parseInt(expiryDateMatcher.group(1)),1); //use the matchers to get positional arguments for month and date

        Customer customer = findCustomerByEmail(customerEmail);
        Address billingAddress = findAddressById(addressId);
        CreditCard creditCard = new CreditCard(cardNumber, cvv, date, customer, billingAddress);

        creditCardRepository.save(creditCard);

        return creditCard;
    }

    /**
     * Likes a review with the customer associated to customer email
     *
     * @param customerEmail email of customer liking the review
     * @param reviewId id of review customer will like
     * @throws EntityNotFoundException when customer does not exist, or when the review does not exist
     * @author Tarek Namani
     */
    @Transactional
    public void likeReview(String customerEmail, int reviewId) {

        Customer customer = findCustomerByEmail(customerEmail);
        Review review = findReviewById(reviewId);


        review.addLikedBy(customer);
        customer.addLikedReview(review);
        customerRepository.save(customer);
        reviewRepository.save(review);

    }

    /**
     * Customer pots a review about a game they bought
     *
     * @param reviewerEmail email of customer writing the reivew
     * @param rating given to the game
     * @param text text body of the review
     * @param purchaseId id associated to review
     * @throws IllegalArgumentException when customer attempts to write a null review, a review with an invalid rating, or a review of a game they do not own
     * @throws EntityNotFoundException when purchase does not exist, or when customer does not exist
     * @author Tarek Namani
     * @return
     */
    @Transactional
    public Review postReview(String reviewerEmail, int rating, String text, int purchaseId) {
        if (text == null) {
            throw new IllegalArgumentException("Review text is null");
        }
        if (rating > 5 || rating < 0) {
            throw new IllegalArgumentException("Review rating is out of range");
        }
        Customer customer = findCustomerByEmail(reviewerEmail);
        Purchase purchase = findPurchaseById(purchaseId); //will call an exception if the purchase does not exist
        if (!purchase.getCustomer().equals(customer))
        {
            throw new IllegalArgumentException("Customer "+customer.getUsername()+ " does not own the game they want to review");
        }


        Review review = new Review(rating, text, purchase);


        reviewRepository.save(review);
        return review;
    }

    /**
     * Allows a manager to reply to a review on a game
     *
     * @param reviewId  id of review to reply to
     * @param replyText body of reply text
     * @param managerId id of manager
     * @throws IllegalArgumentException when manager attempts to write a null reply,
     * @throws EntityNotFoundException when purchase does not exist, or when manager does not exist
     * @author Tarek Namani
     */
    @Transactional
    public void replyToReview(int reviewId, String replyText, int managerId) {

        if (replyText == null) throw new IllegalArgumentException("Reply text is null !");

        Manager manager;
        try {
            manager = findManagerById(managerId);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Only manager can reply to reviews !");
        }
        Review review = findReviewById(reviewId);
        Reply reply = new Reply(replyText, review);
        review.setReply(reply);

        replyRepository.save(reply);
        reviewRepository.save(review);


    }

    /**
     * Checks out a customers cart
     *
     * @param customerEmail email of customer checking out
     * @param addressId id associated to a customers billing address
     * @param creditCardId credit card used to process the payment
     * @author Tarek Namani
     * @throws IllegalArgumentException when customer attempts to check out an empty card, a cart with out of stock or inactive games, or when credit card does not belong to the customer, or is expired
     * @throws EntityNotFoundException when customer, credit card or address do not exist
     */
    @Transactional
    public void checkout(String customerEmail, int addressId, int creditCardId) {
        Customer customer = findCustomerByEmail(customerEmail);
        Address address = findAddressById(addressId);
        CreditCard creditCard = findCreditCardById(creditCardId);

        LocalDate dateOfPurchase = LocalDate.now();

        if (!creditCard.getCustomer().equals(customer)) {
            throw new IllegalArgumentException("Credit card does not belong to this customer");
        }

        if (creditCard.getExpiryDate().isBefore(dateOfPurchase)) {
            throw new IllegalArgumentException("Credit card is expired");
        }


        Set<CartItem> itemsInCart = cartItemRepository.findByCartItemId_Customer_Id(customer.getId());
        if (itemsInCart.isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout an empty cart!");
        }
        Set<Game> gamesInCart = itemsInCart.stream().map(CartItem::getGame).collect(Collectors.toSet());
        itemsInCart.forEach(cartItem -> {
            Game game = cartItem.getGame();
            if (!game.isActive()) throw new IllegalArgumentException("Cannot checkout an inactive game");
            if (game.getStock() <= 0) throw new IllegalArgumentException("Game is out of stock");
            game.setStock(game.getStock() - cartItem.getQuantity());

        });
        gameRepository.saveAll(gamesInCart);
        itemsInCart.stream().forEach(item -> {
            while (item.getQuantity()>0) {
                Purchase purchase = new Purchase(dateOfPurchase,getPromotionalPrice(item.getGame().getId()),item.getGame(),customer,address,creditCard);
                purchaseRepository.save(purchase);
                item.setQuantity(item.getQuantity()-1);
            }
        });
        cartItemRepository.deleteAll(itemsInCart); //remove the game from the customers cart
        customerRepository.save(customer);
    }

    /**
     * gets the current price of a customers cart (including promotions)
     *
     * @param customerEmail email associated to customer
     * @return float price of all the games in the customer's cart
     * @throws EntityNotFoundException when customer does not exist
     * @author Tarek Namani
     */
    @Transactional
    public float getCartPrice(String customerEmail) {

        Customer customer = findCustomerByEmail(customerEmail);
        long price = cartItemRepository.findByCartItemId_Customer_Id(customer.getId()).stream().mapToLong(game -> (long) ((long) game.getQuantity()*getPromotionalPrice(game.getGame().getId()))).sum();
        return (float) price;
    }
    /**
     * gets the current price of a Game(including promotions)
     *
     * @param gameId id associated to a game
     * @return float price of the game
     * @throws EntityNotFoundException when Game does not exist
     * @author Tarek Namani
     */
    @Transactional
    public float getPromotionalPrice(int gameId) {

        Game game = findGameById(gameId);

        LocalDate currentDate = LocalDate.now();
        if (game.getCopyPromotions().isEmpty()) return game.getPrice(); //if there are no promotions


        float originalPrice = game.getPrice();
        int discount = game.getCopyPromotions().stream().filter(promotion ->
                (currentDate.isAfter(promotion.getStartDate().toLocalDate()) && currentDate.isBefore(promotion.getEndDate()
                        .toLocalDate()))).mapToInt(Promotion::getDiscount).sum(); //sum up the active discounts on the game

        if (discount >= 100) return 0; //discount cannot exceed 100%
        return originalPrice * ((float) discount/100);
    }


    /**
     * Returns a set of all the credit cards associated to a customer
     *
     * @param email email associated to a customer
     * @return a set of credit cards associated to a customer
     * @throws EntityNotFoundException when customer does not exist
     * @author Tarek Namani
     */
    @Transactional
    public Set<CreditCard> viewCustomerCreditCards(String email) {
        Customer customer = findCustomerByEmail(email);
        return customer.getCopyofCreditCards();
    }



    /**
     * Returns removes a credit card from a customers wallet
     *
     * @param customerEmail email associated to a customer
     * @param creditCardId email associated to a customer
     * @throws EntityNotFoundException when customer or credit card do not exist
     * @throws IllegalArgumentException when customer does not own the credit card they want to remove, or when it fails to be removed
     * @author Tarek Namani
     */
    @Transactional
    public void removeCreditCardFromWallet(String customerEmail, int creditCardId) {
        Customer customer = findCustomerByEmail(customerEmail);
        CreditCard creditCardToRemove = findCreditCardById(creditCardId);
        var wallet = customer.getCopyofCreditCards();

        if (!wallet.contains(creditCardToRemove)) {
            throw new IllegalArgumentException("Customer is not associated with given credit card");
        }
        boolean success = customer.removeCreditCartFromWallet(creditCardToRemove);

        if(!success){
            throw new IllegalStateException("Failed to delete credit card from customer");
        }
        customerRepository.save(customer);
        creditCardRepository.delete(creditCardToRemove);
    }

    /*
     * Gets a customer's full purchase history
     * @param email the customer's email
     * @return Set<Purchase> the set of all purchases made by this customer
     * @throws IllegalArgumentException if the request is invalid
     * @author Aerin Brown
     */
    @Transactional
    public Set<Purchase> viewCustomerPurchaseHistory(String email) {
        Customer customer = findCustomerByEmail(email);
        return customer.getCopyPurchases();
    }

    /*
     * Gets a customer's full purchase history given a requestor
     * @param email the customer's email
     * @param requestingEmployee the username of the employee making the request
     * @return Set<Purchase> the set of all purchases made by this customer
     * @throws IllegalArgumentException if the request is invalid
     * @author Aerin Brown
     */
    @Transactional
    public Set<Purchase> requestCustomersPurchaseHistory(String customerEmail, String requestingEmployee) {
        // Ensures requestor is actually an employee by fetching them
        // The resulting value is unused, but just makes sure to throw an error if requestor is unauthorized
        findEmployeeByUsername(requestingEmployee);

        return viewCustomerPurchaseHistory(customerEmail);
    }

    /*
     * Creates a refund request
     * @param purchaseId the id of the purchase to be refunded
     * @param reason the reason for the refund
     * @return RefundRequest the newly created refund request
     * @throws IllegalArgumentException if the request is invalid
     * @author Aerin Brown
     */
    @Transactional
    public RefundRequest requestRefund(int purchaseId, String reason) {
        Purchase purchase = findPurchaseById(purchaseId);

        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("No reason given for refund.");
        }
        if (purchase.getRefundRequest() != null) {
            throw new IllegalArgumentException("Purchase already has a refund request");
        }

        RefundRequest request = new RefundRequest(purchase, RequestStatus.PENDING, reason, null);
        refundRepository.save(request);
        return request;
    }

    /*
     * Approves a pending refund request
     * @param refundId the id of the refund to be approved
     * @param approverUsername the username of the approving employee
     * @throws IllegalArgumentException if the request is invalid
     * @author Aerin Brown
     */
    @Transactional
    public void approveRefund(int refundId, String approverUsername) {
        Employee approver = findEmployeeByUsername(approverUsername);
        RefundRequest refund = findRefundById(refundId);

        if (refund.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only pending requests can be approved.");
        }
        if (refund.getReviewer() != approver) {
            throw new IllegalArgumentException("Only employees assigned to the refund request can approve it.");
        }

        refund.setStatus(RequestStatus.APPROVED);
        refundRepository.save(refund);
    }
    /*
     * Denies a pending refund request
     * @param refundId the id of the refund to be denied
     * @param approverUsername the username of the denying employee
     * @throws IllegalArgumentException if the request is invalid
     * @author Aerin Brown
     */
    @Transactional
    public void denyRefund(int refundId, String employeeUsername) {
        Employee employee = findEmployeeByUsername(employeeUsername);
        RefundRequest refund = findRefundById(refundId);

        if (refund.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only pending requests can be denied.");
        }
        if (refund.getReviewer() != employee) {
            throw new IllegalArgumentException("Only employees assigned to the refund request can deny it.");
        }

        refund.setStatus(RequestStatus.DENIED);
        refundRepository.save(refund);
    }

    /*
     * Adds an employee as the reviewer of a refund request
     * @param refundId the refund to add the reviewer to
     * @param reviewerUsername the username of the employee to add as a reviewer
     * @throws IllegalArgumentException if the request is invalid
     * @author Aerin Brown
     */
    @Transactional
    public void addReviewerToRefundRequest(int refundId, String reviewerUsername) {
        Employee reviewer = findEmployeeByUsername(reviewerUsername);
        RefundRequest refund = findRefundById(refundId);

        if (refund.getReviewer() != null) {
            throw new IllegalArgumentException("Refund already has reviewer!");
        }
        if (!reviewer.isActive()) {
            throw new IllegalArgumentException("Cannot assign an inactive employee");
        }

        refund.setReviewer(reviewer);
        refundRepository.save(refund);

        reviewer.addRefundRequest(refund);
        employeeRepository.save(reviewer);
    }

    /*
     * Removes an employee as the reviewer of a refund request
     * @param refundId the refund to remove the reviewer from
     * @param reviewerUsername the username of the employee to remove as a reviewer
     * @throws IllegalArgumentException if the request is invalid
     * @author Aerin Brown
     */
    @Transactional
    public void removeReviewerFromRefundRequest(int refundId, String reviewerUsername) {
        Employee reviewer = findEmployeeByUsername(reviewerUsername);
        RefundRequest refund = findRefundById(refundId);

        if (refund.getReviewer() != reviewer) {
            throw new IllegalArgumentException("Employee is not the reviewer of this refund request.");
        }

        if (!reviewer.getRefundRequests().contains(refund)) {
            throw new IllegalArgumentException("Refund request is not assigned to this employee.");
        }

        refund.setReviewer(null);
        refundRepository.save(refund);

        reviewer.removeRefundRequest(refund);
        employeeRepository.save(reviewer);
    }
}
