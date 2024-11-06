package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

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
    public void checkout(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);

        Set<Game> gamesInCart = customer.getCopyCart();
        if (gamesInCart.isEmpty()) throw new IllegalArgumentException("Cannot checkout an empty cart!");
        customer.getCopyCart().forEach(game -> {
            customer.removeGameFromCart(game);
            game.removeInCartOf(customer);
            gameRepository.save(game);
        });
        customerRepository.save(customer);
    }

    @Transactional
    public float getCartPrice(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);
        long price = customer.getCopyCart().stream().mapToLong(game -> (long) game.getPrice()).sum();
        return (float) price;
    }


    @Transactional
    public Set<CreditCard> viewCustomerCreditCards(String email) {
        Customer customer = findCustomerByEmail(email);
        return customer.getCopyofCreditCards();
    }

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
