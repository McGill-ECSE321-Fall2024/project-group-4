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
public class TestReply {
    @Autowired
    private ReplyRepository replyRepository;
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

    private Reply reply;
    private Customer customer = new Customer("customer", "password", "customer@email.com", "0123456789");
    private Address address = new Address("street", "city", "province", "country", "1A2 B3C", customer);
    private CreditCard creditCard = new CreditCard(1234, 123, LocalDate.now(), customer, address);
    private Game game = new Game("game", "description", "picture", 12, true, 1);
    private Purchase purchase;
    private Review review;

    @AfterEach
    public void tearDown() {
        replyRepository.deleteAll();
        reviewRepository.deleteAll();
        purchaseRepository.deleteAll();
        gameRepository.deleteAll();
        creditCardRepository.deleteAll();
        addressRepository.deleteAll();
        customerRepository.deleteAll();

    }

    @BeforeEach
    public void setUp() {
        customerRepository.save(customer);
        addressRepository.save(address);
        creditCardRepository.save(creditCard);
        gameRepository.save(game);
        purchase = new Purchase(LocalDate.now(), 12, game, customer, address, creditCard);
        purchaseRepository.save(purchase);
        review = new Review(3, "review", purchase);
        reviewRepository.save(review);
        reply = new Reply("reply", review);
        replyRepository.save(reply);
    }

    @Test
    /**
     * Author: Camille Pouliot
     * Description: Tests saving and loading a Reply object from the database
     */
    public void testSaveAndLoadReply() {
        //save

        //load
        Optional<Reply> loadedReplyOpt = replyRepository.findById(reply.getId());

        //compare
        assertTrue(loadedReplyOpt.isPresent());
        Reply loadedReply = loadedReplyOpt.get();
        assertEquals(reply.getText(), loadedReply.getText());
        assertEquals(reply.getReview().getId(), loadedReply.getReview().getId());
    }

    
}
