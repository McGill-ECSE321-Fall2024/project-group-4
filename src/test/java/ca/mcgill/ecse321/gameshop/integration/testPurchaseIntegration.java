package ca.mcgill.ecse321.gameshop.integration;


import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testPurchaseIntegration {

    @Autowired
    private TestRestTemplate client;

    private int gameId;
    private final String gameName = "ECSE 321 game";
    private final String gameDescription = "A game about coding as many lines as possible";
    private final String gamePicture = "pic.url";
    private final float gamePrice = 15.0f;
    private final int stock = 5;

    private final String customerEmail = "validCustomer@email.com";
    private final String customerUsername = "validUser";
    private final String customerPassword = "SafePassword";
    private final String customerPhoneNumber = "123456789";
    private CustomerDTO customerDTO;
    private int customerId = 0;



    private final String customerStreet = "st Catherine";
    private final String customerPostalCode = "HX9 XBX";
    private final String customerCountry = "Canada";
    private final String customerProvince = "Quebec";
    private final String customerCity = "Montreal";
    private final Address validCustomerAddress = new Address(customerStreet,customerCity,customerProvince,customerCountry,customerPostalCode,new Customer(customerUsername,customerPassword,customerEmail,customerPhoneNumber));
    private int addressId = 0;


    private final int creditCardNumber = 123456789;
    private final int cvv = 123;
    private final LocalDate expiryDate = LocalDate.of(2025, 1, 1);
    private final String expiryDateString = "01/25";
    private final CreditCard creditCard = new CreditCard(creditCardNumber, cvv,expiryDate,null,null);
    private int creditCardid =0;
    private int expiredCreditCardId = 0;

    private int promotionID = 0;

    private int purchaseID = 0;

    private int reviewId = 0;



    private GameManagementService gameManagementService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ReplyRepository replyRepository;


    /**
     *
     * Attempt to find a non-existing game in the system
     *
     * @throws JSONException when reading the response JSON
     * @author Tarek Namani
     */
    @Test
    @Order(1)
    public void testFindInvalidGame() throws JSONException {
        //Act
        ResponseEntity<String> response = client.getForEntity("/games/" + 0, String.class);

        //Arrange
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No Game found", new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));
    }

    /**
     * Create a new category for a valid game
     * @author Tarek Namani
     */
    @Test
    @Order(2)
    public void testCreateNewCategory(){
        //Act
        ResponseEntity<Void> response = client.postForEntity("/categories/" + "actionGames", null, void.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(categoryRepository.findByName("actionGames").isPresent());
    }



    /**
     *
     *
     * @author Tarek Namani
     * Tests the functionality of adding a game to the catalogue
     */
    @Test
    @Order(3)
    public void testCreateGame() throws JSONException {
        //Arrage
        GameInputDTO gameInputDTO = new GameInputDTO(gameName,gameDescription,gamePicture,gamePrice,false,stock, List.of("actionGames"));

        //Act
        ResponseEntity<String> response = client.postForEntity("/games", gameInputDTO, String.class);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONObject gameParams = new JSONObject(response.getBody());
        assertEquals(gameName, gameParams.getString("name"));
        assertEquals(String.valueOf(gamePrice), gameParams.getString("price")); //cannot getFloat from a json, convert ref to string instead
        assertEquals(gameDescription, gameParams.getString("description"));
        assertEquals(stock, gameParams.getInt("stock"));
        assertFalse(gameParams.getBoolean("isActive"));
        gameId = gameParams.getInt("id");
        assertTrue(gameRepository.existsById(gameId));

    }

    /**
     * Attempts to successfully find a game by id
     *
     * @throws JSONException  when json is not read properly
     * @author Tarek Namani
     */
    @Test
    @Order(4)
    public void testFindGameById() throws JSONException {
        //Act
        ResponseEntity<String> response = client.getForEntity("/games/" + gameId, String.class);

        //Arrange
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONObject gameParams = new JSONObject(response.getBody());
        assertEquals(gameName, gameParams.getString("name"));
        assertEquals(String.valueOf(gamePrice), gameParams.getString("price")); //cannot getFloat from a json, convert ref to string instead
        assertEquals(gameDescription, gameParams.getString("description"));
        assertEquals(stock, gameParams.getInt("stock"));
        assertFalse(gameParams.getBoolean("isActive"));
    }

    /**
     * @throws JSONException for converting to JSON when asserting
     * @author Tarek Namani
     * Creates a customer
     */
    @Test
    @Order(5)
    public void testCreateCustomer() throws JSONException {
        //Arrange
        Customer customer = new Customer(customerUsername,customerPassword,customerEmail,customerPhoneNumber);
        customerDTO = new CustomerDTO(customer);
        //Act
        ResponseEntity<String> response = client.postForEntity("/accounts/customers/", customerDTO, String.class);

        //Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONObject clientParams = new JSONObject(response.getBody());
        assertEquals(customerUsername, clientParams.getString("username"));
        assertEquals(customerPassword, clientParams.getString("password"));
        assertEquals(customerEmail, clientParams.getString("email"));
        assertEquals(customerPhoneNumber, clientParams.getString("phoneNumber"));
        customerId = customerRepository.findByEmail(customerEmail).get().getId();
        assertTrue(customerRepository.findByEmail(customerEmail).isPresent());

    }

    /**
     * @throws JSONException when JSON conversion does not work
     * @author Tarek Namani
     * tests the creation of an address associated with a customer
     */
    @Test
    @Order(6)
    @Transactional
    public void testCreateAddress() throws JSONException {
        //Arrange
        AddressDTO customerAddress = new AddressDTO(validCustomerAddress);


        //Act
        ResponseEntity<String> response = client.postForEntity("/accounts/customers/"+customerEmail+"/addresses", customerAddress, String.class);

        //Assert
        assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONObject addressParams = new JSONObject(response.getBody());
        assertEquals(customerStreet, addressParams.getString("street"));
        assertEquals(customerCity, addressParams.getString("city"));
        assertEquals(customerProvince, addressParams.getString("province"));
        assertEquals(customerCountry,addressParams.getString("country"));
        assertEquals(customerPostalCode, addressParams.getString("postalCode"));
        assertEquals(1, customerRepository.findByEmail(customerEmail).get().getCopyAddresses().size());
        assertTrue(addressRepository.findById(addressParams.getInt("id")).isPresent());
        addressId = addressParams.getInt("id");
    }


    /**
     * @throws JSONException when JSON conversion does not work
     * @author Tarek Namani
     * tests the creation of a creditCard associated with a customer
     */
    @Order(7)
    @Test
    @Transactional
    public void testCreateCustomerCreditCard() throws JSONException {
        //Arrange
        creditCard.setBillingAddress(validCustomerAddress);
        CreditCardDTO creditCardDTO = new CreditCardDTO(creditCard);
        String url = "/customers/" + customerEmail + "/credit-cards?expiryDate={expiryDate}&addressId={addressId}";
        HttpEntity<CreditCardDTO> requestEntity = new HttpEntity<>(creditCardDTO);

         //Act
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class,expiryDateString,addressId);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONObject creditCardParam = new JSONObject(response.getBody());
        assertEquals(cvv, creditCardParam.getInt("cvv"));
        assertEquals(expiryDate.toString(), creditCardParam.getString("expiryDate"));
        assertEquals(creditCardNumber, creditCardParam.getInt("cardNumber"));
        assertEquals(addressId, creditCardParam.getJSONObject("billingAddress").getInt("id"));
        assertEquals(1, customerRepository.findByEmail(customerEmail).get().getCopyofCreditCards().size());
        creditCardid = creditCardParam.getInt("id");
        assertTrue(creditCardRepository.findById(creditCardid).isPresent());

    }


    /**
     * @throws JSONException when JSON conversion does not work
     * @author Tarek Namani
     * tests the creation of a creditCard with an invalid expiry date
     */
    @Order(8)
    @Test
    public void testCreateInvalidCustomerCreditCard() throws JSONException {
        //Arrange
        creditCard.setBillingAddress(validCustomerAddress);
        CreditCardDTO creditCardDTO = new CreditCardDTO(creditCard);
        String url = "/customers/" + customerEmail + "/credit-cards?expiryDate={expiryDate}&addressId={addressId}";
        HttpEntity<CreditCardDTO> requestEntity = new HttpEntity<>(creditCardDTO);
        String invalidExpiryDate = "invalidExpiryDate";

        //Act
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class,invalidExpiryDate,addressId);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid expiry date, format is MM/YY", new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));


    }

    /**
     * @throws JSONException when JSON conversion does not work
     * @author Tarek Namani
     * tests the creation of a creditCard associated with a customer that does not exist
     */
    @Order(9)
    @Test
    public void testCreateInvalidCustomerCreditCard2() throws JSONException {
        //Arrange
        creditCard.setBillingAddress(validCustomerAddress);
        CreditCardDTO creditCardDTO = new CreditCardDTO(creditCard);
        String url = "/customers/invalidEmail/credit-cards?expiryDate={expiryDate}&addressId={addressId}";
        HttpEntity<CreditCardDTO> requestEntity = new HttpEntity<>(creditCardDTO);

        //Act
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class,expiryDateString,addressId);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No Customer found with email invalidEmail", new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));

    }

    /**
     * @throws JSONException when converting JSON
     * @author Tarek Namaani
     *
     * Tests adding a second (expired credit card) and viewing all credit cards
     */
    @Order(10)
    @Test
    @Transactional
    public void testViewCreditCards() throws JSONException {
        //Arrange
        CreditCard secondCreditCard = new CreditCard(123123,123,LocalDate.of(2010,10,10),null,validCustomerAddress);
        CreditCardDTO creditCardDTO = new CreditCardDTO(secondCreditCard);
        String url = "/customers/" + customerEmail + "/credit-cards?expiryDate={expiryDate}&addressId={addressId}";
        HttpEntity<CreditCardDTO> requestEntity = new HttpEntity<>(creditCardDTO);
        ResponseEntity<String> preResponse = client.exchange(url, HttpMethod.POST, requestEntity, String.class,"10/10",addressId); //adds a second credit card
        expiredCreditCardId = new JSONObject(preResponse.getBody()).getInt("id");

        //Act
        ResponseEntity<String> response = client.getForEntity("/customers/"+customerEmail+"/credit-cards", String.class);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        JSONArray creditCards = new JSONArray(response.getBody());
        assertEquals(2, creditCards.length());
        assertTrue(creditCardRepository.findById(expiredCreditCardId).isPresent());

    }

    /**
     * @author Tarek Namaani
     *
     * Tests viewing the price of a game
     */
    @Order(11)
    @Test
    @Transactional
    public void testViewPromotionalPrices() {
        //Act
        ResponseEntity<String> response = client.getForEntity("/games/" + gameId + "/price", String.class);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(String.valueOf(gamePrice), response.getBody());


    }

    /**
     * @author Tarek Namaani
     *
     * Tests viewing the price of a game that does not exist
     */
    @Order(11)
    @Test
    @Transactional
    public void testViewPromotionalPricesOfInexistentGame() throws JSONException {
        //Act
        ResponseEntity<String> response = client.getForEntity("/games/" + gameId+3 + "/price", String.class);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No Game found with id " + gameId+3,  new JSONObject(String.valueOf(response.getBody())).getJSONArray("errorMessages").get(0));

    }

    /**
     * @author Tarek Namaani
     *
     * Tests viewing the price of items in cart, when there are no items in a customers cart
     */
    @Order(12)
    @Test
    public void testViewCartPrice(){
        //Act
        ResponseEntity<String> response = client.getForEntity("/customers/"+customerEmail+"/cart/price", String.class);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("0.0",  response.getBody());

    }

    /**
     * @author Tarek Namaani
     *
     * Tests viewing the price of items in cart, when there are no items in a customers cart
     */
    @Order(13)
    @Test
    public void testAddPromotionToGame(){
        //Act
        ResponseEntity<String> response = client.getForEntity("/customers/"+customerEmail+"/cart/price", String.class);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("0.0",  response.getBody());

    }

    /**
     * @throws JSONException when converting JSON
     * @author Tarek Namani
     * Tests attemtping to checkout an empty cart
     */
    @Order(14)
    @Test
    public void testCheckoutEmptyCart() throws JSONException {
        //Arrange
        String url = "/customers/"+customerEmail+"/cart?billingAddressId={addressId}&creditCardId={cardId}";

        //Act
        ResponseEntity<String> response = client.exchange(url,HttpMethod.POST, null,String.class, addressId,creditCardid);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot checkout an empty cart!", new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));
    }

    /**
     * @throws JSONException when converting JSON
     * @author Tarek Namani
     * Tests adding multiple games to cart
     */
    @Order(15)
    @Test
    @Transactional
    public void testAddGamesToCart() {
        //Arrange
        String url = "/customers/"+customerId+"/cart/" + gameId;

        //Act
        ResponseEntity<Void> response = client.exchange(url,HttpMethod.PUT, null,void.class);
        client.exchange(url, HttpMethod.PUT, null,void.class);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(cartItemRepository.findByCartItemId_Customer_Id(customerId).isEmpty());
        assertEquals(2, cartItemRepository.findByCartItemId_Customer_IdAndCartItemId_Game_Id(customerId,gameId).get().getQuantity());

    }

    /**
     * @throws JSONException when converting JSON
     * @author Tarek Namani
     * Tests attemtping to checkout inactiveGames from cart
     */
    @Order(16)
    @Test
    public void testCheckoutInactiveGames() throws JSONException {
        //Arrange
        String url = "/customers/"+customerEmail+"/cart?billingAddressId={addressId}&creditCardId={cardId}";

        //Act
        ResponseEntity<String> response = client.exchange(url,HttpMethod.POST, null,String.class, addressId,creditCardid);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot checkout an inactive game", new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));
    }

    /**
     * @throws JSONException when converting JSON
     * @author Tarek Namani
     * Tests setting game to an active state
     */
    @Order(17)
    @Test
    public void testSetActiveGames() {
        //Arrange
        String url = "/games/"+gameId+"/is_active?is_active={isActive}";

        //Act
        ResponseEntity<String> response = client.exchange(url,HttpMethod.PUT, null,String.class, true);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(cartItemRepository.findByCartItemId_Customer_IdAndCartItemId_Game_Id(customerId, gameId).get().getGame().isActive());

    }

    /**
     * @throws JSONException for JSON conversion
     * @author Tarek Namani
     * Asserts that an error is raised when reviewing without a purchase
     */
    @Order(18)
    @Test
    public void testReviewNotPurchasedGame() throws JSONException {
        //Arrange
        String url = "/customers/"+customerEmail+"/reviews?rating={rating}&purchaseId={purchaseId}";
        HttpEntity<String> requestEntity = new HttpEntity<>("I quite liked this game!");

        //Act
        ResponseEntity<String> response = client.exchange(url,HttpMethod.POST, requestEntity,String.class,5,-1);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No Purchase found with id -1" , new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));

    }

    /**
     * @throws JSONException when converting JSON
     * @author Tarek Namani
     * Tests attemtping to checkout with an expired credit Card
     */
    @Order(19)
    @Test
    public void testCheckoutExpiredCard() throws JSONException {
        //Arrange
        String url = "/customers/"+customerEmail+"/cart?billingAddressId={addressId}&creditCardId={cardId}";

        //Act
        ResponseEntity<String> response = client.exchange(url,HttpMethod.POST, null,String.class, addressId,expiredCreditCardId);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Credit card is expired", new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));
    }

    /**
     * @author Tarek Namani
     * Tests the removal of a credit card from a customer's wallet
     */
    @Order(20)
    @Test
    @Transactional
    public void testRemoveCreditCardFromWallet() {
        //Arrange
        String url = "/customers/"+customerEmail+"/credit-cards/" + expiredCreditCardId;

        //Act
        client.exchange(url, HttpMethod.DELETE, null, void.class);


        //Assert
        assertEquals(1,customerRepository.findByEmail(customerEmail).get().getCopyofCreditCards().size());

    }

    /**
     * @author Tarek Namani
     * Tests creating a promotion
     */
    @Order(21)
    @Test
    @Transactional
    public void testCreatePromotion() {
        //Arrange
        String url = "/promotions?startDate={startDate}&endDate={endDate}";
        Promotion promotion = new Promotion(25);
        PromotionDTO promotionDTO = new PromotionDTO(promotion);
        HttpEntity<PromotionDTO> requestEntity = new HttpEntity<>(promotionDTO);
        Date StartDate = Date.valueOf(LocalDate.of(2000,10,10));
        Date endDate = Date.valueOf(LocalDate.of(2050,10,10));

        //Act
        ResponseEntity<PromotionDTO> response =  client.exchange(url, HttpMethod.POST, requestEntity ,PromotionDTO.class, StartDate, endDate);


        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2000-10-09", response.getBody().startDate().toString());
        assertEquals("2050-10-09", response.getBody().endDate().toString());
        assertEquals(25,response.getBody().discount());
        promotionID = response.getBody().id();

    }

    /**
     * @author Tarek Namani
     * Tests adding a game to a promotion and checking cart price
     */
    @Order(22)
    @Test
    @Transactional
    public void testAddGameToPromotion() {
        //Arrange
        ResponseEntity<Void> addedPromotion = client.exchange("/games/"+gameId+"/"+promotionID, HttpMethod.PUT, null ,void.class);
        String url = "/customers/"+customerEmail+"/cart/price";

        //Act
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, null ,String.class);


        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(gamePrice*0.75*2, Float.parseFloat(response.getBody()));

    }

    /**
     * @author Tarek Namani
     * Tests asserting checkout of multiple games in cart
     */
    @Order(23)
    @Test
    @Transactional
    public void testCheckout(){
        //Arrange
        String url = "/customers/"+customerEmail+"/cart?billingAddressId={addressId}&creditCardId={cardId}";

        //Act
        ResponseEntity<Void> response = client.exchange(url,HttpMethod.POST, null,void.class, addressId,creditCardid);

        //Assert
        assertNull(response.getBody());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(2, customerRepository.findByEmail(customerEmail).get().getCopyPurchases().size());
        assertEquals(gameName,customerRepository.findByEmail(customerEmail).get().getCopyPurchases().iterator().next().getGamePurchased().getName());
        purchaseID = customerRepository.findByEmail(customerEmail).get().getCopyPurchases().iterator().next().getId();

    }


    /**
     * @author Tarek Namani
     * Test posting a review to a purchased game
     */
    @Order(24)
    @Test
    @Transactional
    public void testPostReview() {
        //Arrange
        String url = "/customers/"+customerEmail+"/reviews?rating={rating}&purchaseId={purchaseId}";
        HttpEntity<String> requestEntity = new HttpEntity<>("I quite liked this game!");

        //Act
        ResponseEntity<Void> response = client.exchange(url,HttpMethod.POST, requestEntity,void.class,5,purchaseID);

        //Assert
        assertNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Review review = purchaseRepository.findById(purchaseID).get().getReview();
        assertEquals("I quite liked this game!", review.getText());
        assertEquals(5, review.getRating());
        reviewId = review.getId();

    }

    /**
     * @author Tarek Namani
     * Test liking a posted review
     */
    @Order(25)
    @Test
    @Transactional
    public void testLikeReview() {
        //Arrange
        String url = "/customers/"+customerEmail+"/reviews/"+reviewId+"/likes";
        HttpEntity<String> requestEntity = new HttpEntity<>("I quite liked this game!");

        //Act
        ResponseEntity<Void> response = client.exchange(url,HttpMethod.PUT, requestEntity,void.class);

        //Assert
        assertNull(response.getBody());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Review review = purchaseRepository.findById(purchaseID).get().getReview();
        assertEquals(1, review.getCopyLikedBy().size());

    }


    /**
     * @author Tarek Namani
     * Test replying to a posted review
     */
    @Order(26)
    @Test
    @Transactional
    public void testReplyToReview() {
        //Arrange
        Manager manager = new Manager("manager", "manager");
        managerRepository.save(manager);
        String url = "/reviews/"+reviewId+"/reply?managerId={managerId}";
        HttpEntity<String> requestEntity = new HttpEntity<>("Thank you for the review!");

        //Act
        ResponseEntity<Void> response = client.exchange(url,HttpMethod.POST, requestEntity,void.class,manager.getId());

        //Assert
        assertNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Review review = purchaseRepository.findById(purchaseID).get().getReview();
        assertEquals("Thank you for the review!", review.getReply().getText());

    }






    @BeforeAll
    public void cleanUp() {
        replyRepository.deleteAll();
        managerRepository.deleteAll();
        promotionRepository.deleteAll();
        purchaseRepository.deleteAll();
        reviewRepository.deleteAll();
        cartItemRepository.deleteAll();
        creditCardRepository.deleteAll();
        customerRepository.deleteAll();
        categoryRepository.deleteAll();
        gameRepository.deleteAll();
        addressRepository.deleteAll();
        categoryRepository.deleteAll();
    }



}
