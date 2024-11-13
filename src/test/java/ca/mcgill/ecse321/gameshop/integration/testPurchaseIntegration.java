package ca.mcgill.ecse321.gameshop.integration;


import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.dto.CustomerDTO;
import ca.mcgill.ecse321.gameshop.dto.GameInputDTO;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testPurchaseIntegration {

    @Autowired
    private TestRestTemplate client;

    private int gameId = 0;
    private String gameName = "ECSE 321 game";
    private String gameDescription = "A game about coding as many lines as possible";
    private String gamePicture = "pic.url";
    private float gamePrice = 15.0f;
    private int stock = 5;
    private Game game = new Game(gameName,gameDescription,gamePicture,gamePrice,true,stock);
    private GameManagementService gameManagementService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private CustomerRepository customerRepository;


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
        ResponseEntity<String> response = client.getForEntity("/games/" + gameId, String.class);

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
        CustomerDTO customerDTO = new CustomerDTO("username","safePassword","validEmail","122333",null,null,null,null);
        //Act
        ResponseEntity<String> response = client.postForEntity("/accounts/customers/", customerDTO, String.class);

        //Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONObject clientParams = new JSONObject(response.getBody());
        assertEquals("username", clientParams.getString("username"));
        assertEquals("safePassword", clientParams.getString("password"));
        assertEquals("validEmail", clientParams.getString("email"));
        assertEquals("122333", clientParams.getString("phoneNumber"));
        assertTrue(customerRepository.findByEmail("validEmail").isPresent());

    }


    @BeforeAll
    public void cleanUp() {
        customerRepository.deleteAll();
        categoryRepository.deleteAll();
        gameRepository.deleteAll();
    }



}
