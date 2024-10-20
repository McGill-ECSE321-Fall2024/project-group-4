package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Camille Pouliot
 */
@SpringBootTest
public class TestReview {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    private Customer customer = new Customer("customer", "password", "customer@email.com", "0123456789");
    private Address address = new Address("street", "city", "province", "country", "1A2 B3C", customer);
    private CreditCard creditCard = new CreditCard(1234, 123, LocalDate.now(), customer, address);
    private Game game = new Game("game", "description", "picture", 12, true, 1);
    private Purchase purchase = new Purchase(LocalDate.now(), 12, game, customer, address, creditCard);
    private Review review;

    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
        purchaseRepository.deleteAll();
        gameRepository.deleteAll();
        creditCardRepository.deleteAll();
        addressRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        review = new Review();
        review.setPurchase(purchase);
        review.setRating(3);
        review.setText("review");
    }

    @Test
    /**
     * Author: Camille Pouliot
     * Description: Tests saving and loading a Review object from the database
     */
    public void testSaveAndLoadingReview() {
        //save
        customerRepository.save(customer);
        addressRepository.save(address);
        creditCardRepository.save(creditCard);
        gameRepository.save(game);
        purchaseRepository.save(purchase);

        purchase.setReview(review);
        reviewRepository.save(review);
        purchaseRepository.save(purchase);

        //load
        Optional<Review> loadedReviewOpt = reviewRepository.findById(review.getId());

        //compare
        assertTrue(loadedReviewOpt.isPresent());
        Review loadedReview = loadedReviewOpt.get();
        assertEquals(review.getRating(), loadedReview.getRating());
        assertEquals(review.getText(), loadedReview.getText());
        assertEquals(review.getPurchase().getId(), loadedReview.getPurchase().getId());
    }
}
