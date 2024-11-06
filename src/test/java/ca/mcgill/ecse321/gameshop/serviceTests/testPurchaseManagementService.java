package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.PurchaseRepository;
import ca.mcgill.ecse321.gameshop.DAO.ReplyRepository;
import ca.mcgill.ecse321.gameshop.DAO.ReviewRepository;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.PurchaseManagementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class testPurchaseManagementService {

    @InjectMocks
    PurchaseManagementService reviewService;

    @Mock
    ReviewRepository reviewRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    PurchaseRepository purchaseRepository;
    @Mock
    ReplyRepository replyRepository;

    Customer customer = new Customer("customer", "password", "customer@email.com", "0123456789");
    int rating = 3;
    String reviewText = "Average game, money well spent";
    Purchase purchase = new Purchase(null,15,null,customer,null,null);
    Review referenceReview = new Review(rating, reviewText, purchase);



    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
        customerRepository.deleteAll();
        purchaseRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        when(reviewRepository.save(any(Review.class))).thenReturn(referenceReview);
        when(purchaseRepository.findById(anyInt())).thenReturn(Optional.of(purchase));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(reviewRepository.findById(referenceReview.getId())).thenReturn(Optional.of(referenceReview));

        when(reviewRepository.findById(referenceReview.getId())).thenReturn(Optional.of(referenceReview));
        purchase.setPurchasePrice(30);
    }


    @Test
    public void testCreateReview() {
        //Arrange
        Set<Customer> likedBy = new HashSet<>();  //add list of customers who liked review
        likedBy.add(customer);

        //save
        reviewService.likeReview(customer.getEmail(), referenceReview.getId());

        //load
        Optional<Review> loadedReviewOpt = reviewRepository.findById(referenceReview.getId());

        //compare
        assertTrue(loadedReviewOpt.isPresent());
        Review loadedReview = loadedReviewOpt.get();
        assertFalse(loadedReview.getCopyLikedBy().isEmpty());
        List<Customer> likedByCustomersFromDb = new ArrayList<>(loadedReview.getCopyLikedBy());
        assertEquals(customer.getUsername(), likedByCustomersFromDb.get(0).getUsername());

    }

    @Test
    public void testCreateValidReview() {
        //Arrange

        //Act
        Review createdReview = reviewService.createReview(3,reviewText,purchase.getId());

        //Assert
        assertNotNull(createdReview);
        assertEquals(rating, createdReview.getRating());
        assertEquals(purchase.getPurchasePrice(), createdReview.getPurchase().getPurchasePrice());
        assertEquals(reviewText, createdReview.getText());
        verify(reviewRepository, times(1)).save(createdReview);

    }

    @Test
    public void testPostReview() {
        //Arrange


        //Act
        Review postedReview = reviewService.createReview(3,reviewText,purchase.getId());


        //Assert
        assertNotNull(postedReview);
        assertEquals(rating, postedReview.getRating());
        assertEquals(purchase.getPurchasePrice(), postedReview.getPurchase().getPurchasePrice());
        assertEquals(reviewText, postedReview.getText());
        verify(reviewRepository, times(1)).save(postedReview);

    }

    @Test
    public void testPostReviewWithInvalidRating() {
        //Arrange
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->reviewService.postReview(-3,reviewText,purchase.getId()));


        //Assert
        assertEquals("Review rating is out of range", exception.getMessage());
    }

    @Test
    public void replyToReview() {
        //Arrange
        String replyText = "I do not approve of this review";
        Reply referenceReply = new Reply(replyText, referenceReview);

        when(replyRepository.findById(anyInt())).thenReturn(Optional.of(referenceReply));
        when(replyRepository.save(any(Reply.class))).thenReturn(referenceReply);

        //Act
        reviewService.replyToReview(referenceReview.getId(),replyText);

        //Assert
        assertNotNull(referenceReview.getReply());
        assertEquals(referenceReply.getText(),referenceReview.getReply().getText());
        verify(replyRepository, times(1)).save(referenceReview.getReply());

    }
}
