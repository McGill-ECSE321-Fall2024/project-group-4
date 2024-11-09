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


    @Transactional 
    public RefundRequest findRefundById(int id) {
        Optional<RefundRequest> optRefund = refundRepository.findById(id);

        if (optRefund.isPresent()) {
            return optRefund.get();
        }
        throw new EntityNotFoundException("No Refund Request found with id " + id);
    }
    @Transactional
    public Employee findEmployeeByUsername(String username) {
        if (username == null) throw new EntityNotFoundException("Employee username is null!");

        Optional<Employee> optEmployee = employeeRepository.findByUsername(username);
        if (optEmployee.isPresent()) {
            return optEmployee.get();
        }
        throw new EntityNotFoundException("No Employee found with username " + username);
    }

    @Transactional
    public Review findReviewById(int id) {
        Optional<Review> optReview = reviewRepository.findById(id);
        if (optReview.isPresent()) {
            return optReview.get();
        }
        throw new EntityNotFoundException("No Review found with id " + id);
    }

    @Transactional
    public Customer findCustomerByEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email is null!");

        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        if (optCustomer.isPresent()) {
            return optCustomer.get();
        }
        throw new EntityNotFoundException("No Customer found with email " + email);

    }

    @Transactional
    public Purchase findPurchaseById(int id) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(id);
        if (optPurchase.isPresent()) {
            return optPurchase.get();
        }
        throw new EntityNotFoundException("No Purchase found with id " + id);
    }

    @Transactional
    public CreditCard findCreditCardById(int creditCardId) {
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCardId);
        if (optionalCreditCard.isPresent()) {
            return optionalCreditCard.get();
        }
        throw new EntityNotFoundException("No Credit Card found with id " + creditCardId);
    }

    @Transactional
    public Address findAddressById(int addressId) {
        Optional<Address> optAdress = addressRepository.findById(addressId);
        if (optAdress.isPresent()) {
            return optAdress.get();
        }
        throw new EntityNotFoundException("No Address found with id " + addressId);
    }



    @Transactional
    public CreditCard createCreditCard(int cardNumber, String cvv, String expiryDate, String customerEmail, int addressId) {

        Matcher cvvMatcher = Pattern.compile("^\\d{3}$").matcher(cvv); //create a Regex to identify and match valid CVV patterns
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

    @Transactional
    public void likeReview(String customerEmail, int reviewId) {

        Customer customer = findCustomerByEmail(customerEmail);
        Review review = findReviewById(reviewId);


        review.addLikedBy(customer);
        customer.addLikedReview(review);
        customerRepository.save(customer);
        reviewRepository.save(review);

    }

    @Transactional
    public Review postReview(int rating, String text, int purchaseId) {
        if (text == null) {
            throw new IllegalArgumentException("Review text is null");
        }
        if (rating > 5 || rating < 0) {
            throw new IllegalArgumentException("Review rating is out of range");
        }
        Purchase purchase = findPurchaseById(purchaseId); //will call an exception if the purchase does not exist
        Review review = new Review(rating, text, purchase);
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public void replyToReview(int reviewId, String replyText) {

        if (replyText == null) throw new IllegalArgumentException("reply text is null");

        Review review = findReviewById(reviewId);
        Reply reply = new Reply(replyText, review);
        review.setReply(reply);

        replyRepository.save(reply);
        reviewRepository.save(review);


    }

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

        Set<Game> gamesInCart = customer.getCopyCart();
        if (gamesInCart.isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout an empty cart!");
        }

        gamesInCart.forEach(game -> {
            customer.removeGameFromCart(game);
            game.removeInCartOf(customer);
            if (!game.isActive()) throw new IllegalArgumentException("Cannot checkout an inactive game");
            if (game.getStock() == 0) throw new IllegalArgumentException("Game is out of stock");
            game.setStock(game.getStock() - 1);
            gameRepository.save(game);
        }); //remove the games from the customers cart

        gamesInCart.stream().map(game -> new Purchase(dateOfPurchase, applyPromotion(game), game, customer, address, creditCard)).collect(Collectors.toSet()).forEach(purchaseRepository::save);
        customerRepository.save(customer);
    }

    @Transactional
    public float getCartPrice(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);
        long price = customer.getCopyCart().stream().mapToLong(game -> (long) applyPromotion(game)).sum();
        return (float) price;
    }

    @Transactional
    public float applyPromotion(Game game) {

        if (game == null) throw new IllegalArgumentException("Game is null!");
        LocalDate currentDate = LocalDate.now();
        if (game.getCopyPromotions().isEmpty()) return game.getPrice(); //if there are no promotions


        float originalPrice = game.getPrice();
        int discount = game.getCopyPromotions().stream().filter(promotion ->
                (currentDate.isAfter(promotion.getStartDate().toLocalDate()) && currentDate.isBefore(promotion.getEndDate()
                        .toLocalDate()))).mapToInt(activePromotions -> activePromotions.getDiscount()).sum(); //sum up the active discounts on the game

        if (discount >= 100) return 0; //discount cannot exceed 100%
        return originalPrice * ((float) discount/100);
    }

    @Transactional
    public Set<CreditCard> viewCustomerCreditCards(String email) {
        Customer customer = findCustomerByEmail(email);
        return customer.getCopyofCreditCards();
    }

    @Transactional
    public void addCreditCardToCustomerWallet(String customerEmail, int creditCardId) {
        Customer customer = findCustomerByEmail(customerEmail);
        CreditCard creditCard = findCreditCardById(creditCardId);

        creditCard.setCustomer(customer);
        customer.addCreditCardToWallet(creditCard);

        creditCardRepository.save(creditCard);
        customerRepository.save(customer);
    }

    @Transactional
    public void removeCreditCardFromWallet(String customerEmail, int creditCardId) {
        Customer customer = findCustomerByEmail(customerEmail);
        CreditCard creditCard = findCreditCardById(creditCardId);


        if (customer.getCopyofCreditCards().isEmpty()) {
            throw new IllegalArgumentException("Customer doesn't have credit cards!");
        }

        if (customer.getCopyofCreditCards().contains(creditCard) && creditCard.getCustomer() == customer) {
            customer.removeCreditCartFromWallet(creditCard);
            creditCard.setCustomer(null);
            creditCardRepository.save(creditCard);
            customerRepository.save(customer);
            return;
        }

        if (!customer.getCopyofCreditCards().contains(creditCard)) {
            throw new IllegalArgumentException("Customer does not own credit card : " + creditCard.getCardNumber());
        }

        throw new IllegalArgumentException("Credit card is not associated to customer : " + customer.getUsername());


    }

    @Transactional
    public Set<Purchase> viewCustomerPurchaseHistory(String email) {
        Customer customer = findCustomerByEmail(email);
        return customer.getPurchases();
    }

    @Transactional
    public Set<Purchase> requestCustomersPurchaseHistory(String customerEmail, String requestingEmployee) {
        // Ensures requestor is actually an employee by fetching them
        // The resulting value is unused, but just makes sure to throw an error if requestor is unauthorized
        findEmployeeByUsername(requestingEmployee);

        return viewCustomerPurchaseHistory(customerEmail);
    }

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

    @Transactional
    public void approveRefund(int refundId) {
        RefundRequest refund = findRefundById(refundId);

        if (refund.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only pending requests can be approved.");
        }

        refund.setStatus(RequestStatus.APPROVED);
        refundRepository.save(refund);
    }

    @Transactional
    public void denyRefund(int refundId) {
        RefundRequest refund = findRefundById(refundId);

        if (refund.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only pending requests can be denied.");
        }

        refund.setStatus(RequestStatus.DENIED);
        refundRepository.save(refund);
    }

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
