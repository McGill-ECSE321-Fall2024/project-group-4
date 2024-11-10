package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.PurchaseManagementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestPurchaseManagementService {

    @InjectMocks
    PurchaseManagementService purhcaseManagementService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    Customer customer;
    Customer customer2;
    LocalDate date = LocalDate.of(2024,11,5);
    int rating = 3;
    String reviewText = "Average game, money well spent";
    Category category = new Category("actionGame");
    Address cusomterAddress;
    CreditCard creditCard;
    CreditCard creditCard2;
    Game game1;
    Game game2;
    Purchase purchase;
    Review referenceReview;



    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
        customerRepository.deleteAll();
        purchaseRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        //initialize all fields
        customer = new Customer("customer", "password", "customer@email.com", "0123456789");
        customer2 = new Customer("customer2", "password", "secondCustomer@email.com", "0123456789");
        cusomterAddress = new Address("Rue University","Montreal", "Quebec","Canada", "123 4h4", customer);
        creditCard = new CreditCard(123123, 123, LocalDate.of(2025, 10,25),customer, cusomterAddress);
        creditCard2 = new CreditCard(12312233, 12233, LocalDate.of(2025, 10,25),customer2, cusomterAddress);
        game1 = new Game("testGame", "An average game", "example.url",15,true, 5);
        game2 = new Game("testGame2", "A good game", "example.url",30,true, 1);
        purchase = new Purchase(date,15, game1,customer,cusomterAddress,creditCard);
        referenceReview = new Review(rating, reviewText, purchase);

        customer.addAddress(cusomterAddress);
        customer.addCreditCardToWallet(creditCard);
        purchase.setPurchasePrice(30);
        when(reviewRepository.save(any(Review.class))).thenReturn(referenceReview);

        when(purchaseRepository.findById(purchase.getId())).thenReturn(Optional.of(purchase));
        when(purchaseRepository.findById(-5)).thenReturn(Optional.empty());

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail(customer2.getEmail())).thenReturn(Optional.of(customer2));
        when(customerRepository.findByEmail("invalidEmail")).thenReturn(Optional.empty());

        when(reviewRepository.findById(referenceReview.getId())).thenReturn(Optional.of(referenceReview));
        when(reviewRepository.findById(-5)).thenReturn(Optional.empty());

        when(addressRepository.findById(cusomterAddress.getId())).thenReturn(Optional.of(cusomterAddress));
        when(addressRepository.findById(-5)).thenReturn(Optional.empty());

        when(creditCardRepository.findById(0)).thenReturn(Optional.of(creditCard));
        when(creditCardRepository.findById(1)).thenReturn(Optional.of(creditCard2));
        when(creditCardRepository.findById(-5)).thenReturn(Optional.empty());

        when(gameRepository.findById(game1.getId())).thenReturn(Optional.of(game1));
        when(gameRepository.findById(game2.getId())).thenReturn(Optional.of(game2));
        when(gameRepository.findById(-5)).thenReturn(Optional.empty());

    }

    @Test
    public void testFindCustomerByEmail() {
        //Act
        Customer loadedCustomer = purhcaseManagementService.findCustomerByEmail("customer@email.com");

        //Assert
        assertNotNull(loadedCustomer);
        assertEquals("customer@email.com", loadedCustomer.getEmail());
        assertEquals("password", loadedCustomer.getPassword());
        assertEquals("customer", loadedCustomer.getUsername());
        assertEquals("0123456789", loadedCustomer.getPhoneNumber());
        assertEquals(customer, loadedCustomer);
        verify(customerRepository, times(1)).findByEmail("customer@email.com");


    }

    @Test
    public void testFindInvalidCustomerByEmail() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purhcaseManagementService.findCustomerByEmail("invalidEmail"));

        //Assert
        assertEquals("No Customer found with email invalidEmail", exception.getMessage());
        verify(customerRepository, times(1)).findByEmail("invalidEmail");

    }

    @Test
    public void testFindInvalidCustomerByEmail2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purhcaseManagementService.findCustomerByEmail(null));

        //Assert
        assertEquals("Email is null!", exception.getMessage());
        verify(customerRepository, times(0)).findByEmail(null);

    }

    @Test
    public void testFindPurchaseById() {
        //Act
        Purchase laodedPurchase = purhcaseManagementService.findPurchaseById(purchase.getId());

        //Assert
        assertNotNull(laodedPurchase);
        assertEquals(purchase.getId(), laodedPurchase.getId());
        assertEquals(purchase.getPurchasePrice(), laodedPurchase.getPurchasePrice());
        assertEquals(purchase.getCustomer(), laodedPurchase.getCustomer());
        assertEquals(purchase.getPaymentMethod(), laodedPurchase.getPaymentMethod());
        assertEquals(purchase.getGamePurchased(), laodedPurchase.getGamePurchased());
        verify(purchaseRepository, times(1)).findById(purchase.getId());


    }


    @Test
    public void testFindInvalidPurchaseById() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purhcaseManagementService.findPurchaseById(-5));

        //Assert
        assertEquals("No Purchase found with id -5", exception.getMessage());
        verify(purchaseRepository, times(1)).findById(-5);

    }

    @Test
    public void testFindReviewByID() {
        //Act
        Review loadedReview = purhcaseManagementService.findReviewById(referenceReview.getId());

        //Assert
        assertNotNull(loadedReview);
        assertEquals(referenceReview.getText(), loadedReview.getText());
        assertEquals(referenceReview.getPurchase(), loadedReview.getPurchase());
        assertEquals(referenceReview.getId(), loadedReview.getId());
        assertEquals(referenceReview.getRating(), loadedReview.getRating());


        verify(reviewRepository, times(1)).findById(referenceReview.getId());


    }

    @Test
    public void testFindInvalidReviewByID() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purhcaseManagementService.findReviewById(-5));

        //Assert
        assertEquals("No Review found with id -5", exception.getMessage());
        verify(reviewRepository, times(1)).findById(-5);

    }

    @Test
    public void testFindAddressById() {
        //Act
        Address loadedAddress = purhcaseManagementService.findAddressById(referenceReview.getId());

        //Assert
        assertNotNull(loadedAddress);
        assertEquals(cusomterAddress.getId(), loadedAddress.getId());
        assertEquals(cusomterAddress.getCity(), loadedAddress.getCity());
        assertEquals(cusomterAddress.getCountry(), loadedAddress.getCountry());
        assertEquals(cusomterAddress.getCustomer(), loadedAddress.getCustomer());
        assertEquals(cusomterAddress.getPostalCode(), loadedAddress.getPostalCode());
        assertEquals(cusomterAddress.getProvince(), loadedAddress.getProvince());
        verify(addressRepository, times(1)).findById(cusomterAddress.getId());

    }

    @Test
    public void testFindInvalidAddressById() {
        //Act
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class ,()->purhcaseManagementService.findAddressById(-5));

        //Assert
        assertEquals("No Address found with id -5", exception.getMessage());
        verify(addressRepository, times(1)).findById(-5);

    }


    @Test
    public void testFindCreditCardById() {
        //Act
        CreditCard loadedCreditCard = purhcaseManagementService.findCreditCardById(referenceReview.getId());

        //Assert
        assertNotNull(loadedCreditCard);
        assertEquals(creditCard.getId(), loadedCreditCard.getId());
        assertEquals(creditCard.getCardNumber(), loadedCreditCard.getCardNumber());
        assertEquals(creditCard.getExpiryDate(), loadedCreditCard.getExpiryDate());
        assertEquals(creditCard.getCvv(), loadedCreditCard.getCvv());
        assertEquals(creditCard.getCustomer(), loadedCreditCard.getCustomer());
        verify(creditCardRepository, times(1)).findById(creditCard.getId());

    }

    @Test
    public void testFindInvalidCreditCardById() {
        //Act
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class ,()->purhcaseManagementService.findCreditCardById(-5));

        //Assert
        assertEquals("No Credit Card found with id -5", exception.getMessage());
        verify(creditCardRepository, times(1)).findById(-5);

    }


    @Test
    public void testCreateCreditCard() {
        //Act
        CreditCard createdCreditCard = purhcaseManagementService.createCreditCard(creditCard.getCardNumber(), creditCard.getCvv(), creditCard.getExpiryDate(), creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId());

        //Assert
        assertNotNull(createdCreditCard);
        assertEquals(creditCard.getCvv(), createdCreditCard.getCvv());
        assertEquals(creditCard.getExpiryDate(), createdCreditCard.getExpiryDate());
        assertEquals(creditCard.getCustomer().getEmail(), createdCreditCard.getCustomer().getEmail());
        assertEquals(creditCard.getBillingAddress().getId(), createdCreditCard.getBillingAddress().getId());
        verify(creditCardRepository, times(1)).save(createdCreditCard);

    }

    @Test
    public void testLikeReview() {
        //Arrange
        Set<Customer> likedBy = new HashSet<>();  //add list of customers who liked review
        likedBy.add(customer);

        //save
        purhcaseManagementService.likeReview(customer.getEmail(), referenceReview.getId());

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
    public void testCreateReview() {
        //Arrange

        //Act
        Review createdReview = purhcaseManagementService.postReview(3,reviewText,purchase.getId());

        //Assert
        assertNotNull(createdReview);
        assertEquals(rating, createdReview.getRating());
        assertEquals(purchase.getPurchasePrice(), createdReview.getPurchase().getPurchasePrice());
        assertEquals(reviewText, createdReview.getText());
        verify(reviewRepository, times(1)).save(createdReview);

    }

    @Test
    public void testCreateInvalidReviewWithNullString() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purhcaseManagementService.postReview(3,null,purchase.getId()));

        //Assert
        assertEquals("Review text is null", exception.getMessage());

    }

    @Test
    public void testCreateInvalidReviewWithInvalidRating() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purhcaseManagementService.postReview(8,"Some text",purchase.getId()));

        //Assert
        assertEquals("Review rating is out of range", exception.getMessage());

    }

    @Test
    public void testCreateInvalidReviewWithInvalidRating2() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purhcaseManagementService.postReview(-2,"Some text",purchase.getId()));

        //Assert
        assertEquals("Review rating is out of range", exception.getMessage());

    }

    @Test
    public void testPostReview() {
        //Arrange

        //Act
        Review postedReview = purhcaseManagementService.postReview(3,reviewText,purchase.getId());


        //Assert
        assertNotNull(postedReview);
        assertEquals(rating, postedReview.getRating());
        assertEquals(purchase.getPurchasePrice(), postedReview.getPurchase().getPurchasePrice());
        assertEquals(reviewText, postedReview.getText());
        verify(reviewRepository, times(1)).save(postedReview);

    }

    @Test
    public void testRplyToReview() {
        //Arrange
        String replyText = "I do not approve of this review";
        Reply referenceReply = new Reply(replyText, referenceReview);

        when(replyRepository.findById(anyInt())).thenReturn(Optional.of(referenceReply));
        when(replyRepository.save(any(Reply.class))).thenReturn(referenceReply);

        //Act
        purhcaseManagementService.replyToReview(referenceReview.getId(),replyText);

        //Assert
        assertNotNull(referenceReview.getReply());
        assertEquals(referenceReply.getText(),referenceReview.getReply().getText());
        verify(replyRepository, times(1)).save(referenceReview.getReply());

    }

    @Test
    public void testInvalidReplyToReview() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purhcaseManagementService.replyToReview(referenceReview.getId(),null));

        //Assert
        assertEquals("reply text is null", exception.getMessage());

    }

    @Test
    public void testViewCustomerCreditCards() {
        //Act
        Set<CreditCard> creditCards = purhcaseManagementService.viewCustomerCreditCards(customer.getEmail());

        //Assert
        assertNotNull(creditCards);
        assertEquals(1, creditCards.size());
        assertTrue(customer.getCopyofCreditCards().containsAll(creditCards));


    }

    @Test
    public void testAddCreditCardToCustomerWallet() {
        //Act
        purhcaseManagementService.addCreditCardToCustomerWallet(customer.getEmail(),1);

        //Assert
        assertEquals(2, customer.getCopyofCreditCards().size());

    }

    @Test
    public void TestRemoveCreditCardFromCustomerWallet() {
        //Act
        purhcaseManagementService.removeCreditCardFromWallet(customer.getEmail(),0);

        //Assert
        assertEquals(0, customer.getCopyofCreditCards().size());

    }

    @Test
    public void TestRemoveCreditCardFromWrongCustomerWallet() {
        //Arrange
        creditCard.setCustomer(customer2);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> purhcaseManagementService.removeCreditCardFromWallet(customer.getEmail(),creditCard.getId()));

        //Assert
        assertEquals("Credit card is not associated to customer : customer", exception.getMessage());

    }

    @Test
    public void TestRemoveCreditCardFromEmptyCustomerWallet() {
        //Arrange
        customer.removeCreditCartFromWallet(creditCard);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> purhcaseManagementService.removeCreditCardFromWallet(customer.getEmail(),creditCard.getId()));

        //Assert
        assertEquals("Customer doesn't have credit cards!", exception.getMessage());

    }

    @Test
    public void TestRemoveWrongCreditCardFromEmptyCustomerWallet() {
        //Arrange
        customer2.addCreditCardToWallet(creditCard);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> purhcaseManagementService.removeCreditCardFromWallet(customer.getEmail(),1));

        //Assert
        assertEquals("Customer does not own credit card : 12312233", exception.getMessage());

    }

    @Test
    public void testApplyPromotion() {
        //Arrange
        Promotion promotion = new Promotion(50);
        promotion.setStartDate(Date.valueOf(LocalDate.of(2004,12,2)));
        promotion.setEndDate(Date.valueOf(LocalDate.of(2040,12,2)));//apply a 50% promotion to the game
        promotion.addGame(game1);
        game1.addPromotion(promotion);

        //Act
        float discountedPrice = purhcaseManagementService.applyPromotion(game1, LocalDate.of(2024,10,10));

        //Assert
        assertEquals(game1.getPrice()/2, discountedPrice); //asssert that the cost is reduced by half
    }

    @Test
    public void testApplyFreeGamePromotion() {
        //Arrange
        Promotion promotion = new Promotion(110);
        promotion.setStartDate(Date.valueOf(LocalDate.of(2004,12,2)));
        promotion.setEndDate(Date.valueOf(LocalDate.of(2040,12,2)));//apply a 110% promotion to the game
        promotion.addGame(game1);
        game1.addPromotion(promotion);

        //Act
        float discountedPrice = purhcaseManagementService.applyPromotion(game1, LocalDate.of(2024,10,10));

        //Assert
        assertEquals(0f, discountedPrice); //assert that the game is free (only 100% was applied)
    }

    @Test
    public void testApplyPrommotionWhenPromotionIsOver() {
        //Arrange
        Promotion promotion = new Promotion(50);
        promotion.addGame(game1);
        promotion.setStartDate(Date.valueOf(LocalDate.of(2004,1,2)));
        promotion.setEndDate(Date.valueOf(LocalDate.of(2040,1,2)));//apply a 50% promotion to the game

        //Act
        float discountedPrice = purhcaseManagementService.applyPromotion(game1, LocalDate.of(2044,10,10));

        //Assert
        assertEquals(game1.getPrice(), discountedPrice); //asssert that the cost did not change
    }

    @Test
    public void testApplyPromotionWithNoPromotion() {
        //Act
        float discountedPrice = purhcaseManagementService.applyPromotion(game1, LocalDate.of(2044,10,10));

        //Assert
        assertEquals(game1.getPrice(), discountedPrice); //asssert that the cost did not change
    }

    @Test
    public void testApplyPromotionWithInvalidGame() {


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.applyPromotion(null, LocalDate.of(2024,10,10)));

        //Assert
        assertEquals("Game is null!",exception.getMessage());
    }

    @Test
    public void testApplyPromotionWithInvalidDate() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.applyPromotion(game1, null));

        //Assert
        assertEquals("Date is null!",exception.getMessage());
    }

    @Test
    public void testCheckoutWithWrongCreditCard() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), 1, LocalDate.of(2004,10,20)));

        //Assert
        assertEquals("Credit card does not belong to this customer", exception.getMessage());

    }

    @Test
    public void testCheckoutWithExpiredCreditCard() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId(), LocalDate.of(2050,10,20)));

        //Assert
        assertEquals("Credit card is expired", exception.getMessage());

    }

    @Test
    public void testCheckoutWithNullDate() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId(), null));

        //Assert
        assertEquals("Purchase date is null", exception.getMessage());

    }

    @Test
    public void testCheckoutEmptyCart() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId(),LocalDate.of(2024,10,10)));

        //Assert
        assertEquals("Cannot checkout an empty cart!", exception.getMessage());

    }

    @Test
    public void testCheckoutInacitveGame() {
        //Arrange
        game1.setActive(false);
        when(cartItemRepository.findByCartItemId_Customer_Id(customer.getId()))
                .thenReturn(Set.of(new CartItem(1, customer, game1)));

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId(),LocalDate.of(2024,10,10)));

        //Assert
        assertEquals("Cannot checkout an inactive game", exception.getMessage());

    }

    @Test
    public void testCheckoutOutOfStockgame() {
        //Arrange
        when(cartItemRepository.findByCartItemId_Customer_Id(customer.getId()))
                .thenReturn(Set.of(new CartItem(1, customer, game1)));
        game1.setStock(0);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purhcaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId(),LocalDate.of(2024,10,10)));

        //Assert
        assertEquals("Game is out of stock", exception.getMessage());

    }

    @Test
    public void testCheckoutCart1() {
        //Arrange
        int initialStockGame1 = game1.getStock();
        int initialStockGame2 = game2.getStock();
        //since persistence layer is not active, both games will have the same ID (both 0)
        //as a result, we are mocking as if the game2 has id of 1 and game1 has id of 0
        Game spyGame = spy(game2);
        when(spyGame.getId()).thenReturn(game1.getId()+1);


        Set<CartItem> customerCart = new HashSet<>();
        customerCart.add(new CartItem(1, customer, game1));
        customerCart.add(new CartItem(1, customer, spyGame));

        when(cartItemRepository.findByCartItemId_Customer_Id(customer.getId()))
                .thenReturn(customerCart);

        doAnswer(invocation -> {
            customerCart.clear();
            return null;
        }).when(cartItemRepository).deleteAll(anyIterable());

        //Act
        purhcaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId(), LocalDate.of(2024,10,10));

        //Assert
        assertEquals(initialStockGame1 - 1, game1.getStock());
        assertEquals(initialStockGame2 - 1, spyGame.getStock());

        assertTrue(customerCart.isEmpty());

        assertEquals(3, customer.getPurchases().size()); //note that we already have a purchase in customer from initialization

        verify(customerRepository, times(1)).save(customer);
        verify(gameRepository, times(1)).save(game1);
        verify(gameRepository, times(1)).save(spyGame);
        verify(purchaseRepository, times(2)).save(any(Purchase.class));
    }

    @Test
    public void testGetCartPrice() {
        //Arrange
        when(cartItemRepository.findByCartItemId_Customer_Id(customer.getId()))
                .thenReturn(Set.of(new CartItem(1, customer, game2)));

        float cartPrice = purhcaseManagementService.getCartPrice(customer.getEmail(), LocalDate.of(2024,10,10));

        assertEquals( game2.getPrice() , cartPrice);
    }
}
