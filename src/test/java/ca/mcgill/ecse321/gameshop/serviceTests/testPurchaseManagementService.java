package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.PurchaseManagementService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class testPurchaseManagementService {

    @InjectMocks
    PurchaseManagementService purchaseManagementService;

    @Mock
    ReviewRepository reviewRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    PurchaseRepository purchaseRepository;
    @Mock
    ReplyRepository replyRepository;
    @Mock
    AddressRepository addressRepository;
    @Mock
    CreditCardRepository creditCardRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    RefundRequestRepository refundRepository;
    @Mock
    private ManagerRepository managerRepository;

    Customer customer;
    Customer customer2;
    LocalDate date = LocalDate.of(2024,11,5);
    int rating = 3;
    String reviewText = "Average game, money well spent";
    String refundText = "Buggy game";
    Category category = new Category("actionGame");
    Address cusomterAddress;
    CreditCard creditCard;
    CreditCard creditCard2;
    Game game;
    Game game2;
    Purchase purchase;
    Review referenceReview;
    Employee validEmployee;
    RefundRequest referenceRequest;
    Employee inactiveEmployee;
    Purchase purchase2;
    RefundRequest approvedRequest;
    RefundRequest deniedRequest;
    Manager manager;



    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
        customerRepository.deleteAll();
        purchaseRepository.deleteAll();
        refundRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        //initialize all fields
        customer = new Customer("customer", "password", "customer@email.com", "0123456789");

        Customer originalCustomer2 = new Customer("customer2", "password", "secondCustomer@email.com", "0123456789");
        customer2 = spy(originalCustomer2);
        when(customer2.getId()).thenReturn(customer.getId() + 1);

        manager = new Manager();
        cusomterAddress = new Address("Rue University","Montreal", "Quebec","Canada", "123 4h4", customer);

        creditCard = new CreditCard(123123, "123", LocalDate.of(2025, 10,1),customer, cusomterAddress);
        CreditCard originalCreditCard2 = new CreditCard(12312233, "124", LocalDate.of(2025, 10,1),customer2, cusomterAddress);
        creditCard2 = spy(originalCreditCard2);
        when(creditCard2.getId()).thenReturn(creditCard.getId() + 1);

        game = new Game("testGame", "An average game", "example.url",15,true, 5);

        Game originalGame2 = new Game("testGame2", "A good game", "example.url",30,true, 1);
        game2 = spy(originalGame2);
        when(game2.getId()).thenReturn(game.getId() + 1);

        purchase = new Purchase(date,15,game,customer,cusomterAddress,creditCard);
        Purchase originalPurchase2 = new Purchase(date,15,game,customer,cusomterAddress,creditCard);
        purchase2 = spy(originalPurchase2);
        when(purchase2.getId()).thenReturn(purchase.getId() + 1);

        referenceReview = new Review(rating, reviewText, purchase);
        validEmployee = new Employee("employee", "password", true);
        inactiveEmployee = new Employee("inactive", "password", false);


        referenceRequest = new RefundRequest(purchase, RequestStatus.PENDING, refundText, null);

        RefundRequest originalApprovedRequest = new RefundRequest(purchase, RequestStatus.APPROVED, refundText, validEmployee);
        approvedRequest = spy(originalApprovedRequest);
        when(approvedRequest.getId()).thenReturn(referenceRequest.getId()+1);

        RefundRequest originalDeniedRequest = new RefundRequest(purchase, RequestStatus.DENIED, refundText, validEmployee);
        deniedRequest = spy(originalDeniedRequest);
        when(deniedRequest.getId()).thenReturn(referenceRequest.getId()+2);

        validEmployee.addRefundRequest(approvedRequest);

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

        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
        when(gameRepository.findById(game2.getId())).thenReturn(Optional.of(game2));
        when(gameRepository.findById(-5)).thenReturn(Optional.empty());

        when(refundRepository.save(any(RefundRequest.class))).thenReturn(referenceRequest);
        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);

        when(employeeRepository.findByUsername(validEmployee.getUsername())).thenReturn(Optional.of(validEmployee));
        when(employeeRepository.findByUsername((inactiveEmployee.getUsername()))).thenReturn(Optional.of(inactiveEmployee));
        when(employeeRepository.findByUsername("invalidUsername")).thenReturn(Optional.empty());
        
        when(refundRepository.findById(referenceRequest.getId())).thenReturn(Optional.of(referenceRequest));
        when(refundRepository.findById(approvedRequest.getId())).thenReturn(Optional.of(approvedRequest));
        when(refundRepository.findById(deniedRequest.getId())).thenReturn(Optional.of(deniedRequest));
        when(refundRepository.findById(-1)).thenReturn(Optional.empty());

        when(managerRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(managerRepository.findById(-1)).thenReturn(Optional.empty());



    }

    @Test
    public void testFindEmployeeByEmail() {
        //Act
        Employee loadedEmployee = purchaseManagementService.findEmployeeByUsername("employee");

        //Assert
        assertNotNull(loadedEmployee);
        assertEquals("employee", loadedEmployee.getUsername());
        assertEquals("password", loadedEmployee.getPassword());
        assertTrue(loadedEmployee.isActive());
        verify(employeeRepository, times(1)).findByUsername("employee");
    }

    @Test
    public void testFindInvalidEmployeeByUsername() {
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findEmployeeByUsername("asdf"));

        //Assert
        assertEquals("No Employee found with username asdf", exception.getMessage());
        verify(employeeRepository, times(1)).findByUsername("asdf");
    }

    @Test
    public void testFindInvalidEmployeeByUsername2() {
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findEmployeeByUsername(null));

        //Assert
        assertEquals("Employee username is null!", exception.getMessage());
    }

    @Test
    public void testFindRefundById() {
        //Act
        RefundRequest refund = purchaseManagementService.findRefundById(referenceRequest.getId());

        //Assert
        assertNotNull(refund);
        assertEquals(referenceRequest.getId(), refund.getId());
        assertEquals(refundText, refund.getReason());
        assertEquals(RequestStatus.PENDING, refund.getStatus());
        assertNull(refund.getReviewer());
        verify(refundRepository, times(1)).findById(referenceRequest.getId());
    }

    @Test
    public void testFindInvalidGameById() {
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findGameById(-5));

        //Assert
        assertEquals("No Game found with id -5", exception.getMessage());
        verify(gameRepository, times(1)).findById(-5);
    }

    @Test
    public void testFindGameById() {
        //Act
        Game loadedGame = purchaseManagementService.findGameById(game.getId());

        //Assert
        assertNotNull(loadedGame);
        assertEquals(game.getId(), loadedGame.getId());
        assertEquals(game.getName(), loadedGame.getName());
        assertEquals(game.getDescription(), loadedGame.getDescription());
        assertEquals(game.getPrice(), loadedGame.getPrice());
        assertEquals(game.getStock(), loadedGame.getStock());
        assertEquals(game.getCopyInCartOf(), loadedGame.getCopyInCartOf());
        assertEquals(game.getCoverPicture(), loadedGame.getCoverPicture());
        assertEquals(game.getCopyPromotions(), loadedGame.getCopyPromotions());
        verify(gameRepository, times(1)).findById(game.getId());

    }

    @Test
    public void testFindInvalidRefundById() {
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findRefundById(-1));

        //Assert
        assertEquals("No Refund Request found with id -1", exception.getMessage());
        verify(refundRepository, times(1)).findById(null);
    }


    @Test
    public void testFindManagerById() {
        //Act
        Manager loadedManager = purchaseManagementService.findManagerById(manager.getId());

        //Assert
        assertNotNull(loadedManager);
        assertEquals(manager.getId(), loadedManager.getId());
        assertEquals(manager.getUsername(), loadedManager.getUsername());
        assertEquals(manager.getPassword(), loadedManager.getPassword());
        verify(managerRepository, times(1)).findById(manager.getId());
    }

    @Test
    public void testFindInvalidManagerByUsername() {
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findManagerById(-1));

        //Assert
        assertEquals("No manager found with id -1", exception.getMessage());
        verify(managerRepository, times(1)).findById(-1);
    }

    @Test
    public void testRequestRefund() {
        //Act
        RefundRequest request = purchaseManagementService.requestRefund(purchase2.getId(), refundText);

        //Assert
        assertNotNull(request);
        assertEquals(purchase2, request.getPurchase());
        assertEquals(refundText, request.getReason());
        assertNull(request.getReviewer());
        assertEquals(RequestStatus.PENDING, request.getStatus());
        verify(refundRepository, times(1)).save(request);
    }

    @Test
    public void testRequestInvalidRefund() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.requestRefund(purchase.getId(), ""));

        //Assert
        assertEquals("No reason given for refund.", exception.getMessage());
    }

    @Test
    public void testRequestInvalidRefund2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.requestRefund(purchase.getId(), "asdf"));

        //Assert
        assertEquals("Purchase already has a refund request", exception.getMessage());
    }

    @Test
    public void testApproveRefund() {
        //Act
        purchaseManagementService.approveRefund(referenceRequest.getId());

        //Assert
        assertEquals(RequestStatus.APPROVED, referenceRequest.getStatus());
    }

    @Test
    public void testApproveInvalidRefund() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.approveRefund(approvedRequest.getId()));

        //Assert
        assertEquals("Only pending requests can be approved.", exception.getMessage());
    }

    @Test
    public void testApproveInvalidRefund2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.approveRefund(deniedRequest.getId()));

        //Assert
        assertEquals("Only pending requests can be approved.", exception.getMessage());
    }

    @Test
    public void testDenyRefund() {
        //Act
        purchaseManagementService.approveRefund(referenceRequest.getId());

        //Assert
        assertEquals(RequestStatus.DENIED, referenceRequest.getStatus());
    }

    @Test
    public void testDenyInvalidRefund() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.approveRefund(approvedRequest.getId()));

        //Assert
        assertEquals("Only pending requests can be denied.", exception.getMessage());
    }

    @Test
    public void testDenyInvalidRefund2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.approveRefund(deniedRequest.getId()));

        //Assert
        assertEquals("Only pending requests can be denied.", exception.getMessage());
    }

    @Test
    public void testAddReviewerToRefund() {
        //Act
        purchaseManagementService.addReviewerToRefundRequest(referenceRequest.getId(), validEmployee.getUsername());

        //Assert
        assertEquals(validEmployee, referenceRequest.getReviewer());
        assertTrue(validEmployee.getRefundRequests().contains(referenceRequest));
    }

    @Test
    public void testAddInvalidReviewerToRefund() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.addReviewerToRefundRequest(deniedRequest.getId(), validEmployee.getUsername()));

        //Assert
        assertEquals("Refund already has reviewer!", exception.getMessage());
    }

    @Test
    public void testAddInvalidReviewerToRefund2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.addReviewerToRefundRequest(referenceRequest.getId(), inactiveEmployee.getUsername()));

        //Assert
        assertEquals("Cannot assign an inactive employee", exception.getMessage());
    }

    @Test
    public void testRemoveReviewerFromRefund() {
        //Act
        purchaseManagementService.removeReviewerFromRefundRequest(approvedRequest.getId(), validEmployee.getUsername());

        //Assert
        assertNull(approvedRequest.getReviewer());
        assertEquals(0, validEmployee.getRefundRequests().size());
    }

    @Test
    public void testRemoveInvalidReviewerFromRefund()  {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.removeReviewerFromRefundRequest(referenceRequest.getId(), validEmployee.getUsername()));

        //Assert
        assertEquals("Employee is not the reviewer of this refund request.", exception.getMessage());
    }

    @Test 
    public void testRemoveInvalidReviewerFromRefund2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.removeReviewerFromRefundRequest(deniedRequest.getId(), validEmployee.getUsername()));

        //Assert
        assertEquals("Refund request is not assigned to this employee.", exception.getMessage());
    }

    @Test
    public void testFindCustomerByEmail() {
        //Act
        Customer loadedCustomer = purchaseManagementService.findCustomerByEmail("customer@email.com");

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
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findCustomerByEmail("invalidEmail"));

        //Assert
        assertEquals("No Customer found with email invalidEmail", exception.getMessage());
        verify(customerRepository, times(1)).findByEmail("invalidEmail");

    }

    @Test
    public void testFindInvalidCustomerByEmail2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManagementService.findCustomerByEmail(null));

        //Assert
        assertEquals("Email is null!", exception.getMessage());
        verify(customerRepository, times(0)).findByEmail(null);

    }

    @Test
    public void testFindPurchaseById() {
        //Act
        Purchase laodedPurchase = purchaseManagementService.findPurchaseById(purchase.getId());

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
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findPurchaseById(-5));

        //Assert
        assertEquals("No Purchase found with id -5", exception.getMessage());
        verify(purchaseRepository, times(1)).findById(-5);

    }

    @Test
    public void testFindReviewByID() {
        //Act
        Review loadedReview = purchaseManagementService.findReviewById(referenceReview.getId());

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
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseManagementService.findReviewById(-5));

        //Assert
        assertEquals("No Review found with id -5", exception.getMessage());
        verify(reviewRepository, times(1)).findById(-5);

    }

    @Test
    public void testFindAddressById() {
        //Act
        Address loadedAddress = purchaseManagementService.findAddressById(referenceReview.getId());

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
        EntityNotFoundException exception = assertThrows( EntityNotFoundException.class ,()->purchaseManagementService.findAddressById(-5));

        //Assert
        assertEquals("No Address found with id -5", exception.getMessage());
        verify(addressRepository, times(1)).findById(-5);

    }


    @Test
    public void testFindCreditCardById() {
        //Act
        CreditCard loadedCreditCard = purchaseManagementService.findCreditCardById(referenceReview.getId());

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
        EntityNotFoundException exception = assertThrows( EntityNotFoundException.class ,()->purchaseManagementService.findCreditCardById(-5));

        //Assert
        assertEquals("No Credit Card found with id -5", exception.getMessage());
        verify(creditCardRepository, times(1)).findById(-5);

    }


    @Test
    public void testAddCreditCardToCustomerWallet() {
        //Act
        CreditCard createdCreditCard = purchaseManagementService.addCreditCardToCustomerWallet(creditCard.getCardNumber(), creditCard.getCvv(), "10/25", creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId());

        //Assert
        assertNotNull(createdCreditCard);
        assertEquals(creditCard.getCvv(), createdCreditCard.getCvv());
        assertEquals(creditCard.getExpiryDate(), createdCreditCard.getExpiryDate());
        assertEquals(creditCard.getCustomer().getEmail(), createdCreditCard.getCustomer().getEmail());
        assertEquals(creditCard.getBillingAddress().getId(), createdCreditCard.getBillingAddress().getId());
        verify(creditCardRepository, times(1)).save(createdCreditCard);

    }

    @Test
    public void addCreditCardToCustomerWalletWithInvalidDate1() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->purchaseManagementService.addCreditCardToCustomerWallet(creditCard.getCardNumber(), creditCard.getCvv(), "10/a", creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId()));

        //Assert
        assertEquals("Invalid expiry date, format is MM/YY", exception.getMessage());
    }

    @Test
    public void addCreditCardToCustomerWalletWithInvalidDate2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->purchaseManagementService.addCreditCardToCustomerWallet(creditCard.getCardNumber(), creditCard.getCvv(), "a/10", creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId()));

        //Assert
        assertEquals("Invalid expiry date, format is MM/YY", exception.getMessage());
    }

    @Test
    public void addCreditCardToCustomerWalletWithInvalidDate3() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->purchaseManagementService.addCreditCardToCustomerWallet(creditCard.getCardNumber(), creditCard.getCvv(), "10123", creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId()));

        //Assert
        assertEquals("Invalid expiry date, format is MM/YY", exception.getMessage());
    }

    @Test
    public void addCreditCardToCustomerWalletWithInvalidDate4() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->purchaseManagementService.addCreditCardToCustomerWallet(creditCard.getCardNumber(), creditCard.getCvv(), "0/0", creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId()));

        //Assert
        assertEquals("Invalid expiry date, format is MM/YY", exception.getMessage());
    }

    @Test
    public void addCreditCardToCustomerWalletWithInvalidCVV1() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->purchaseManagementService.addCreditCardToCustomerWallet(creditCard.getCardNumber(), "Invalid CVV", "10/2", creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId()));

        //Assert
        assertEquals("Invalid cvv number, enter a 3 digit CVV", exception.getMessage());
    }

    @Test
    public void addCreditCardToCustomerWalletWithInvalidCVV2() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->purchaseManagementService.addCreditCardToCustomerWallet(creditCard.getCardNumber(), "1234", "10/2", creditCard.getCustomer().getEmail(),creditCard.getBillingAddress().getId()));

        //Assert
        assertEquals("Invalid cvv number, enter a 3 digit CVV", exception.getMessage());
    }

    @Test
    public void testLikeReview() {
        //Arrange
        Set<Customer> likedBy = new HashSet<>();  //add list of customers who liked review
        likedBy.add(customer);

        //save
        purchaseManagementService.likeReview(customer.getEmail(), referenceReview.getId());

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
        Review createdReview = purchaseManagementService.postReview(customer.getEmail(),3,reviewText,purchase.getId());

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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purchaseManagementService.postReview(customer.getEmail(),3,null,purchase.getId()));

        //Assert
        assertEquals("Review text is null", exception.getMessage());

    }

    @Test
    public void testCreateInvalidReviewWithInvalidRating() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purchaseManagementService.postReview(customer.getEmail(),8,"Some text",purchase.getId()));

        //Assert
        assertEquals("Review rating is out of range", exception.getMessage());

    }

    @Test
    public void testCreateInvalidReviewWithInvalidRating2() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purchaseManagementService.postReview(customer.getEmail(),-2,"Some text",purchase.getId()));

        //Assert
        assertEquals("Review rating is out of range", exception.getMessage());

    }

   @Test
   public void testPostReviewWithwrongUser() {
       //Arrange

       //Act
       IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purchaseManagementService.postReview(customer2.getEmail(),2,"Some text",purchase.getId()));

       //Assert
       assertEquals("Customer customer2 does not own the game they want to review", exception.getMessage());

   }

    @Test
    public void testRplyToReview() {
        //Arrange
        String replyText = "I do not approve of this review";
        Reply referenceReply = new Reply(replyText, referenceReview);

        when(replyRepository.findById(anyInt())).thenReturn(Optional.of(referenceReply));
        when(replyRepository.save(any(Reply.class))).thenReturn(referenceReply);

        //Act
        purchaseManagementService.replyToReview(referenceReview.getId(),replyText, manager.getId());

        //Assert
        assertNotNull(referenceReview.getReply());
        assertEquals(referenceReply.getText(),referenceReview.getReply().getText());
        verify(replyRepository, times(1)).save(referenceReview.getReply());

    }

    @Test
    public void testInvalidReplyToReview() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purchaseManagementService.replyToReview(referenceReview.getId(),null, manager.getId()));

        //Assert
        assertEquals("Reply text is null !", exception.getMessage());

    }

    @Test
    public void testInvalidReplyToReview2() {
        //Arrange

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class , () ->purchaseManagementService.replyToReview(referenceReview.getId(),"goodText", 1));

        //Assert
        assertEquals("Only manager can reply to reviews !", exception.getMessage());

    }

    @Test
    public void testViewCustomerCreditCards() {
        //Act
        Set<CreditCard> creditCards = purchaseManagementService.viewCustomerCreditCards(customer.getEmail());

        //Assert
        assertNotNull(creditCards);
        assertEquals(1, creditCards.size());
        assertTrue(customer.getCopyofCreditCards().containsAll(creditCards));


    }


    @Test
    public void TestRemoveCreditCardFromCustomerWallet() {
        //Act
        purchaseManagementService.removeCreditCardFromWallet(customer.getEmail(),0);

        //Assert
        assertEquals(0, customer.getCopyofCreditCards().size());

    }

    @Test
    public void TestRemoveCreditCardFromWrongCustomerWallet() {
        //Arrange
        creditCard.setCustomer(customer2);
        customer.removeCreditCartFromWallet(creditCard);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> purchaseManagementService.removeCreditCardFromWallet(customer.getEmail(),creditCard.getId()));

        //Assert
        assertEquals("Customer is not associated with given credit card", exception.getMessage());

    }

    @Test
    public void TestRemoveCreditCardFromEmptyCustomerWallet() {
        //Arrange
        customer.removeCreditCartFromWallet(creditCard);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> purchaseManagementService.removeCreditCardFromWallet(customer.getEmail(),creditCard.getId()));

        //Assert
        assertEquals("Customer is not associated with given credit card", exception.getMessage());

    }

    @Test
    public void TestRemoveWrongCreditCardFromEmptyCustomerWallet() {
        //Arrange
        customer2.addCreditCardToWallet(creditCard);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> purchaseManagementService.removeCreditCardFromWallet(customer.getEmail(),1));

        //Assert
        assertEquals("Customer is not associated with given credit card", exception.getMessage());

    }

    @Test
    public void testGetPromotionalPrice1() {


        //Arrange
        Promotion promotion = new Promotion(50);
        promotion.setStartDate(Date.valueOf(LocalDate.of(2004,12,2)));
        promotion.setEndDate(Date.valueOf(LocalDate.of(2040,12,2)));//apply a 50% promotion to the game
        promotion.addGame(game);
        game.addPromotion(promotion);

        //Act
        float discountedPrice = purchaseManagementService.getPromotionalPrice(game.getId());

        //Assert
        assertEquals(game.getPrice()/2, discountedPrice); //asssert that the cost is reduced by half
    }

    @Test
    public void testApplyFreeGamePromotion() {
        //Arrange
        Promotion promotion = new Promotion(110);
        promotion.setStartDate(Date.valueOf(LocalDate.of(2004,12,2)));
        promotion.setEndDate(Date.valueOf(LocalDate.of(2040,12,2)));//try to apply a 110% promotion to the game
        promotion.addGame(game);
        game.addPromotion(promotion);

        //Act
        float discountedPrice = purchaseManagementService.getPromotionalPrice(game.getId());

        //Assert
        assertEquals(0f, discountedPrice); //assert that the game is free (only 100% was applied)
    }

    @Test
    public void testApplyPrommotionWhenPromotionIsOver() {
        //Arrange
        Promotion promotion = new Promotion(50);
        promotion.addGame(game);
        promotion.setStartDate(Date.valueOf(LocalDate.of(2004,1,2)));
        promotion.setEndDate(Date.valueOf(LocalDate.of(2005,1,2)));//apply a 50% promotion to the game

        //Act
        float discountedPrice = purchaseManagementService.getPromotionalPrice(game.getId());

        //Assert
        assertEquals(game.getPrice(), discountedPrice); //asssert that the cost did not change
    }

    @Test
    public void testGetPromotionalPrice2() {
        //Act
        float discountedPrice = purchaseManagementService.getPromotionalPrice(game.getId());

        //Assert
        assertEquals(game.getPrice(), discountedPrice); //asssert that the cost did not change
    }

    @Test
    public void testCheckoutWithWrongCreditCard() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purchaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), 1));

        //Assert
        assertEquals("Credit card does not belong to this customer", exception.getMessage());

    }

    @Test
    public void testCheckoutWithExpiredCreditCard() {
        //Arrange
        customer.addGameToCart(game);
        creditCard.setExpiryDate(LocalDate.of(1999,10,19));

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purchaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId()));

        //Assert
        assertEquals("Credit card is expired", exception.getMessage());

    }

    @Test
    public void testCheckoutEmptyCart() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purchaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId()));

        //Assert
        assertEquals("Cannot checkout an empty cart!", exception.getMessage());

    }

    @Test
    public void testCheckoutInacitveGame() {
        //Arrange
        game.setActive(false);
        customer.addGameToCart(game);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purchaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId()));

        //Assert
        assertEquals("Cannot checkout an inactive game", exception.getMessage());

    }

    @Test
    public void testCheckoutOutOfStockgame() {
        //Arrange
        customer.addGameToCart(game);
        game.setStock(0);

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->purchaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId()));

        //Assert
        assertEquals("Game is out of stock", exception.getMessage());

    }

    @Test
    public void testCheckoutCart1() {
        //Arrange
        int initialStockGame1 = game.getStock();
        int initialStockGame2 = game2.getStock();
        customer.addGameToCart(game);
        customer.addGameToCart(game2);
        game.addInCartOf(customer);
        game2.addInCartOf(customer);

        //Act
        purchaseManagementService.checkout(customer.getEmail(), cusomterAddress.getId(), creditCard.getId());

        //Arrange
        assertEquals(initialStockGame1 - 1, game.getStock());
        assertEquals(initialStockGame2 - 1, game2.getStock());

        assertTrue(game.getCopyInCartOf().isEmpty());
        assertTrue(game2.getCopyInCartOf().isEmpty());
        assertTrue(customer.getCopyCart().isEmpty());

        assertEquals(4, customer.getCopyPurchases().size()); //note the two games originally in the cart from intialization

        verify(customerRepository, times(1)).save(customer);
        verify(gameRepository, times(1)).save(game);
        verify(gameRepository, times(1)).save(game2);
        verify(purchaseRepository, times(2)).save(any(Purchase.class));

    }

    @Test
    public void testGetCartPrice() {
        //Arrange
        customer.addGameToCart(game2);

        float cartPrice = purchaseManagementService.getCartPrice(customer.getEmail());

        assertEquals( game2.getPrice() , cartPrice);
    }
}
