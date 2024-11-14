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
     * Get a game from id
     *
     * @param gameId
     * @return Game
     *
     * @author
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
     * Get review from id
     *
     * @param id
     * @return Review
     *
     * @author
     */
    @Transactional
    public Review findReviewById(int id) {
        Optional<Review> optReview = reviewRepository.findById(id);
        if (optReview.isPresent()) {
            return optReview.get();
        }
        throw new EntityNotFoundException("No Review found with id " + id);
    }

    /**
     * Get a customer from email
     *
     * @param email
     * @return Customer
     *
     * @author
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
     * Get the manager from id
     *
     * @param id
     * @return Manager
     *
     * @author
     */
    @Transactional
    public Manager findManagerById(int id) {
        Optional<Manager> optManager = managerRepository.findById(id);
        if (optManager.isPresent()) {
            return optManager.get();
        }
        throw new EntityNotFoundException("No manager found with id " + id);

    }

    /**
     * Get purchase from id
     *
     * @param id
     * @return Purchase
     *
     * @author
     */
    @Transactional
    public Purchase findPurchaseById(int id) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(id);
        if (optPurchase.isPresent()) {
            return optPurchase.get();
        }
        throw new EntityNotFoundException("No Purchase found with id " + id);
    }

    /**
     * Get credit card from id
     *
     * @param creditCardId
     * @return CreditCard
     *
     * @author
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
     * Get address from id
     *
     * @param addressId
     * @return Address
     *
     * @author
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
     * Add a credit card to a customer wallet
     *
     * @param cardNumber
     * @param cvv
     * @param expiryDate
     * @param customerEmail
     * @param addressId
     * @return CreditCard
     *
     * @author
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
     * Like a review
     *
     * @param customerEmail
     * @param reviewId
     *
     * @author
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
     * Post a review
     *
     * @param reivewerEmail
     * @param rating
     * @param text
     * @param purchaseId
     * @return Review
     *
     * @author
     */
    @Transactional
    public Review postReview(String reivewerEmail, int rating, String text, int purchaseId) {
        if (text == null) {
            throw new IllegalArgumentException("Review text is null");
        }
        if (rating > 5 || rating < 0) {
            throw new IllegalArgumentException("Review rating is out of range");
        }
        Customer customer = findCustomerByEmail(reivewerEmail);
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
     * Reply to a review
     *
     * @param reviewId
     * @param replyText
     * @param managerId
     *
     * @author
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
     * Checkout a customer cart/ Purchase a customer cart
     *
     * @param customerEmail
     * @param addressId
     * @param creditCardId
     *
     * @author
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
        cartItemRepository.deleteAll(itemsInCart); //remove the game from the customers cart
        gameRepository.saveAll(gamesInCart);
        gamesInCart.stream().map(game -> new Purchase(dateOfPurchase, getPromotionalPrice(game.getId()), game, customer, address, creditCard)).collect(Collectors.toSet()).forEach(purchaseRepository::save);
        customerRepository.save(customer);
    }

    /**
     * Get the price of a customer cart
     *
     * @param customerEmail
     * @return float
     *
     * @author
     */
    @Transactional
    public float getCartPrice(String customerEmail) {

        Customer customer = findCustomerByEmail(customerEmail);
        long price = cartItemRepository.findByCartItemId_Customer_Id(customer.getId()).stream().map(CartItem::getGame).mapToLong(game -> (long) getPromotionalPrice(game.getId())).sum();
        return (float) price;
    }

    /**
     * Get price of a game with promotion
     *
     * @param gameId
     * @return float
     *
     * @author
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
     * Get credit card of a customer
     *
     * @param email
     * @return Set<CreditCard>
     *
     * @author
     */
    @Transactional
    public Set<CreditCard> viewCustomerCreditCards(String email) {
        Customer customer = findCustomerByEmail(email);
        return customer.getCopyofCreditCards();
    }


    /**
     * Remove a credit card from a customer wallet
     *
     * @param customerEmail
     * @param creditCardId
     *
     * @author
     */
    @Transactional
    public void removeCreditCardFromWallet(String customerEmail, int creditCardId) {
        Customer customer = findCustomerByEmail(customerEmail);
        CreditCard creditCardToRemove = findCreditCardById(creditCardId);
        var wallet = customer.getCopyofCreditCards();

        if (!wallet.contains(creditCardToRemove)) {
            throw new IllegalArgumentException("Customer is not associated with given credit card");
        }

        if(!customer.removeCreditCartFromWallet(creditCardToRemove)){
            throw new IllegalStateException("Failed to delete credit card from customer");
        }
        customerRepository.save(customer);
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
