package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.*;

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

    private final Customer customer = new Customer("customer", "password", "customer@email.com", "0123456789");
    private final Address address = new Address("street", "city", "province", "country", "1A2 B3C", customer);
    private final CreditCard creditCard = new CreditCard(1234, 123, LocalDate.now(), customer, address);
    private final Game game = new Game("game", "description", "picture", 12, true, 1);
    private final Purchase purchase = new Purchase(LocalDate.now(), 12, game, customer, address, creditCard);
    private Review review;

    @AfterEach
    public void tearDown() {
        purchaseRepository.deleteAll();
        creditCardRepository.deleteAll();
        addressRepository.deleteAll();
        customerRepository.deleteAll();
        gameRepository.deleteAll();
        reviewRepository.deleteAll();
     }

    @BeforeEach
    public void setUp() {
        customerRepository.save(customer);
        addressRepository.save(address);
        creditCardRepository.save(creditCard);
        gameRepository.save(game);
        purchaseRepository.save(purchase);

        review = new Review(3, "review", purchase);
        reviewRepository.save(review);
    }
    @Test
    /**
     * Author: Camille Pouliot
     * Description: Tests saving and loading a Review object from the database
     */
    public void testSaveAndLoadingReview() {

        //load
        Optional<Review> loadedReviewOpt = reviewRepository.findById(review.getId());

        //compare
        assertTrue(loadedReviewOpt.isPresent());
        Review loadedReview = loadedReviewOpt.get();
        assertEquals(review.getRating(), loadedReview.getRating());
        assertEquals(review.getText(), loadedReview.getText());
        assertEquals(review.getPurchase().getId(), loadedReview.getPurchase().getId());
    }

    @Test @Transactional //test should be transational to properly initialze many to many associations
    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading the likedBy association from databases
     */
    public void testSaveAndLoadLikedBy() {
        //add list of customers who liked review


        review.addLikedBy(customer);
        reviewRepository.save(review);




        //load
        Optional<Review> loadedReviewOpt = reviewRepository.findById(review.getId());

        //compare
        assertTrue(loadedReviewOpt.isPresent());
        Review loadedReview = loadedReviewOpt.get();
        List<Customer> likedByCustomersFromDb = new ArrayList<>(loadedReview.getCopyLikedBy());

        assertFalse(likedByCustomersFromDb.isEmpty());
        assertTrue(loadedReview.containsLikedBy(customer));

    }
}
