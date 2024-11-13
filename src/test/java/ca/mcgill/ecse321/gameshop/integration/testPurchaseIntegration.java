package ca.mcgill.ecse321.gameshop.integration;


import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.dto.AddressDTO;
import ca.mcgill.ecse321.gameshop.dto.CreditCardDTO;
import ca.mcgill.ecse321.gameshop.dto.CustomerDTO;
import ca.mcgill.ecse321.gameshop.dto.GameInputDTO;
import ca.mcgill.ecse321.gameshop.model.Address;
import ca.mcgill.ecse321.gameshop.model.CreditCard;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import jakarta.transaction.Transactional;
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

    private final int creditCardNumber = 123456789;
    private final int cvv = 123;
    private final LocalDate expiryDate = LocalDate.of(2025, 1, 1);
    private final String expiryDateString = "01/25";

    private final String customerStreet = "st Catherine";
    private final String customerPostalCode = "HX9 XBX";
    private final String customerCountry = "Canada";
    private final String customerProvince = "Quebec";
    private final String customerCity = "Montreal";
    private final Address validCustomerAddress = new Address(customerStreet,customerCity,customerProvince,customerCountry,customerPostalCode,new Customer(customerUsername,customerPassword,customerEmail,customerPhoneNumber));
    private final CreditCard creditCard = new CreditCard(creditCardNumber, cvv,expiryDate,null,validCustomerAddress);
    private int addressId = 0;

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
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
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
        customerDTO = new CustomerDTO(customerUsername,customerPassword,customerEmail,customerPhoneNumber,null,null,null,null);
        //Act
        ResponseEntity<String> response = client.postForEntity("/accounts/customers/", customerDTO, String.class);

        //Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONObject clientParams = new JSONObject(response.getBody());
        assertEquals(customerUsername, clientParams.getString("username"));
        assertEquals(customerPassword, clientParams.getString("password"));
        assertEquals(customerEmail, clientParams.getString("email"));
        assertEquals(customerPhoneNumber, clientParams.getString("phoneNumber"));
        assertTrue(customerRepository.findByEmail(customerEmail).isPresent());

    }

    @Test
    @Order(6)
    @Transactional
    public void testCreateAddress() throws JSONException {
        //Arrange
        AddressDTO customerAddress = new AddressDTO(validCustomerAddress);


        //Act
        ResponseEntity<String> response = client.postForEntity("/accounts/customers/"+customerEmail+"/addresses", customerAddress, String.class);

        //Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONObject addressParams = new JSONObject(response.getBody());
        assertEquals(customerStreet, addressParams.getString("street"));
        assertEquals(customerCity, addressParams.getString("city"));
        assertEquals(customerProvince, addressParams.getString("province"));
        assertEquals(customerCountry,addressParams.getString("country"));
        assertEquals(customerPostalCode, addressParams.getString("postalCode"));
        assertTrue(customerRepository.findByEmail(customerEmail).get().getCopyAddresses().size()==1);
        assertTrue(addressRepository.findById(addressParams.getInt("id")).isPresent());
        addressId = addressParams.getInt("id");
    }


    @Order(7)
    @Test
    public void testCreateCustomerCreditCard() {
        //Arrange
        creditCard.setBillingAddress(validCustomerAddress);
         CreditCardDTO creditCardDTO = new CreditCardDTO(creditCard);
         String url = "/customers/" + customerEmail + "/credit-cards?expiryDate={expiryDate}&addressId={addressId}";
        HttpEntity<CreditCardDTO> requestEntity = new HttpEntity<>(creditCardDTO);

         //Act
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class,expiryDateString,addressId);

        //Assert
        assertNotNull(response.getBody());
    }


    @BeforeAll
    public void cleanUp() {
        creditCardRepository.deleteAll();
        customerRepository.deleteAll();
        categoryRepository.deleteAll();
        gameRepository.deleteAll();
        addressRepository.deleteAll();
        categoryRepository.deleteAll();
    }



}
