package ca.mcgill.ecse321.gameshop.integration;


import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.dto.GameDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
        assertEquals("No Game found with id 0", new JSONObject(response.getBody()).getJSONArray("errorMessages").get(0));
    }

    /**
     * Attempts to successfully find a game by id
     *
     * @throws JSONException  when json is not read properly
     * @author Tarek Namani
     */
    @Test
    @Order(2)
    public void testFindGameById() throws JSONException {
        //Arrange
        gameRepository.save(game); //replace with createGame eventually


        //Act
        ResponseEntity<String> response = client.getForEntity("/games/" + game.getId(), String.class);

        //Arrange
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        JSONObject gameParams = new JSONObject(response.getBody());
        assertEquals(gameName, gameParams.getString("name"));
        assertEquals(String.valueOf(gamePrice), gameParams.getString("price")); //cannot getFloat from a json, convert ref to string instead
        assertEquals(gameDescription, gameParams.getString("description"));
        assertEquals(stock, gameParams.getInt("stock"));
        assertTrue(gameParams.getBoolean("isActive"));
    }


    @AfterAll
    public void cleanUp() {
        gameRepository.deleteAll();
    }



}
