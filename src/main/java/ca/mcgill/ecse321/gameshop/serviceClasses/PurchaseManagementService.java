package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;
import java.util.Set;
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

    @Transactional
    public Review findReviewById(int id) {
        Optional<Review> optReview = reviewRepository.findById(id);
        if (optReview.isPresent()) {
            return optReview.get();
        }
        throw new IllegalArgumentException("No Review found with id " + id);
    }

    @Transactional
    public Customer findCustomerByEmail(String email) {
        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        if (optCustomer.isPresent()) {
            return optCustomer.get();
        }
        throw new IllegalArgumentException("No Customer found with email " + email);

    }

    @Transactional
    public Purchase findPurchaseById(int id) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(id);
        if (optPurchase.isPresent()) {
            return optPurchase.get();
        }
        throw new IllegalArgumentException("No Purchase found with id " + id);
    }

    @Transactional
    public CreditCard findByCreditCardId(int creditCardId) {
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCardId);
        if (optionalCreditCard.isPresent()) {
            return optionalCreditCard.get();
        }
        throw new IllegalArgumentException("No Credit Card found with id " + creditCardId);
    }

    @Transactional
    public Address findAddressById(int addressId) {
        Optional<Address> optAdress = addressRepository.findById(addressId);
        if (optAdress.isPresent()) {
            return optAdress.get();
        }
        throw new IllegalArgumentException("No Address found with id " + addressId);
    }

    @Transactional
    public Review createReview(int rating, String text, int purchaseId) {

        Purchase purchase = findPurchaseById(purchaseId);
        Review review = new Review(rating, text, purchase);
        reviewRepository.save(review);

        return review;

    }

    @Transactional
    public CreditCard createCreditCard(int cardNumber, int cvv, LocalDate expiryDate, String customerEmail, int addressId) {
        Customer customer = findCustomerByEmail(customerEmail);
        Address billingAddress = findAddressById(addressId);

        CreditCard creditCard = new CreditCard(cardNumber, cvv, expiryDate, customer, billingAddress);

        creditCardRepository.save(creditCard);

        return creditCard;
    }

    @Transactional
    public void likeReview(String customerEmail, int reviewId ) {

        Customer customer = findCustomerByEmail(customerEmail);
        Review review = findReviewById(reviewId);


        review.addLikedBy( customer );
        customer.addLikedReview( review );
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
        Purchase purchase = findPurchaseById(purchaseId);
        Review review = createReview(rating, text, purchaseId);
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public void replyToReview(int reviewId, String replyText) {

        if (replyText == null) throw new IllegalArgumentException("reply text is null");

        Review review = findReviewById(reviewId);
        Reply reply = new Reply(replyText,review);
        review.setReply(reply);

        replyRepository.save(reply);
        reviewRepository.save(review);


    }

    @Transactional
    public void checkout(String customerEmail, int addressId, int creditCardId, LocalDate dateOfPurchase) {
        Customer customer = findCustomerByEmail(customerEmail);
        Address address = findAddressById(addressId);
        CreditCard creditCard = findByCreditCardId(creditCardId);

        if (!creditCard.getCustomer().equals(customer)) {
            throw new IllegalArgumentException("Credit card does not belong to this customer");
        }

        Set<Game> gamesInCart = customer.getCopyCart();
        if (gamesInCart.isEmpty()) throw new IllegalArgumentException("Cannot checkout an empty cart!");

        gamesInCart.forEach(game -> {
            customer.removeGameFromCart(game);
            game.removeInCartOf(customer);
            gameRepository.save(game);
        }); //remove the games from the customers cart

        gamesInCart.stream().map(game -> new Purchase(dateOfPurchase,applyPromotion(game,dateOfPurchase) ,game,customer,address,creditCard )).collect(Collectors.toSet()).forEach(purchaseRepository::save);
        customerRepository.save(customer);
    }

    @Transactional
    public float getCartPrice(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);
        long price = customer.getCopyCart().stream().mapToLong(game -> (long) game.getPrice()).sum();
        return (float) price;
    }

    @Transactional
    public float applyPromotion (Game game, LocalDate currentDate) {

        if (game == null) throw new IllegalArgumentException("Game is null!");
        if (currentDate==null) throw new IllegalArgumentException("Date is null!");
        if (game.getCopyPromotions().isEmpty()) return game.getPrice(); //if there are no promotions


        float originalPrice = game.getPrice();
        double discount = game.getCopyPromotions().stream().filter(promotion ->
                        (currentDate.isAfter(promotion.getStartDate().toLocalDate()) && currentDate.isBefore(promotion.getEndDate()
                                .toLocalDate()))).mapToDouble(activePromotions -> Double.parseDouble(activePromotions.getDiscount())).sum(); //sum up the active discounts on the game

        if (discount >= 1) discount = 1; //discount cannot exceed 100%
        discount = 1-discount; //convert to a 0 to 1 range

        return (float) (discount * originalPrice);
    }

    @Transactional
    public Set<CreditCard> viewCustomerCreditCards(String email) {
        Customer customer = findCustomerByEmail(email);
        return customer.getCopyofCreditCards();
    }

    @Transactional
    public void addCreditCardToCustomerWallet(String customerEmail, int creditCardId) {
        Customer customer = findCustomerByEmail(customerEmail);
        CreditCard creditCard = findByCreditCardId(creditCardId);

        creditCard.setCustomer(customer);
        customer.addCreditCardToWallet(creditCard);

        creditCardRepository.save(creditCard);
        customerRepository.save(customer);
    }

    @Transactional
    public void removeCreditCardFromWallet(String customerEmail, int creditCardId) {
        Customer customer = findCustomerByEmail(customerEmail);
        CreditCard creditCard = findByCreditCardId(creditCardId);


        if (customer.getCopyofCreditCards().isEmpty()) {
            throw new IllegalArgumentException("Customer doesn't have credit cards!");
        }

        if (customer.getCopyofCreditCards().contains(creditCard) && creditCard.getCustomer() == customer) {
            customer.getCopyofCreditCards().remove(creditCard);
            creditCard.setCustomer(null);
            creditCardRepository.save(creditCard);
            customerRepository.save(customer);
        }

        if (!customer.getCopyofCreditCards().contains(creditCard)) {
            throw new IllegalArgumentException("Customer does now own credit card : " + creditCard.getCardNumber());
        }

        throw new IllegalArgumentException("Credit card is not associated to customer : " + customer.getUsername());


    }


}
