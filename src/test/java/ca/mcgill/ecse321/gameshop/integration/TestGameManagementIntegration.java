package ca.mcgill.ecse321.gameshop.integration;

import ca.mcgill.ecse321.gameshop.DAO.CartItemRepository;
import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.dto.GameInputDTO;
import ca.mcgill.ecse321.gameshop.dto.GameResponseDTO;
import ca.mcgill.ecse321.gameshop.model.Category;
import ca.mcgill.ecse321.gameshop.model.Game;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestGameManagementIntegration {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private CartItemRepository cartItemRepo;


    private static final String TEST_CATEGORY_NAME1 = "test category name1";
    private static final String TEST_CATEGORY_NAME2 = "test category name2";
    private int gameId;


    @AfterAll
    public void clear(){
        categoryRepo.deleteAll();
        gameRepo.deleteAll();
        cartItemRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateInvalidCategory1(){
        ResponseEntity<Void> response = client.postForEntity("/categories/" + TEST_CATEGORY_NAME1, null, void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(categoryRepo.findByName(TEST_CATEGORY_NAME1).isPresent());
    }

    @Test
    @Order(2)
    public void testCreateInvalidCategory(){
        ResponseEntity<Void> response = client.postForEntity("/categories/" + TEST_CATEGORY_NAME1, null, void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void testCreateNewCategory2(){
        ResponseEntity<Void> response = client.postForEntity("/categories/" + TEST_CATEGORY_NAME2, null, void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(categoryRepo.findByName(TEST_CATEGORY_NAME2).isPresent());
    }

    @Test
    @Order(4)
    public void testCreateNewGame(){
        //Arrange
        GameInputDTO gameToMake = new GameInputDTO("test game", "test description",
                "test coverPicture", 24.99f, true, 100, List.of(TEST_CATEGORY_NAME1));

        //Act
        ResponseEntity<GameResponseDTO> response = client.postForEntity("/games", gameToMake, GameResponseDTO.class);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameToMake.price(), response.getBody().price());
        assertEquals(gameToMake.name(), response.getBody().name());
        assertEquals(gameToMake.description(), response.getBody().description());
        assertEquals(gameToMake.coverPicture(), response.getBody().coverPicture());
        assertEquals(gameToMake.isActive(), response.getBody().isActive());
        assertEquals(1, response.getBody().categories().size());
        assertEquals(gameToMake.categories().get(0), response.getBody().categories().stream().findFirst().get().name());
        assertTrue(gameRepo.existsById(response.getBody().id()));

        gameId = response.getBody().id();
    }

    @Test
    @Order(5)
    public void testCreateNewGameInInvalidCategory(){
        //Arrange
        GameInputDTO gameToMake = new GameInputDTO("new game", "test description",
                "test coverPicture", 24.99f, true, 100, List.of("INVALID CATEGORY"));

        //Act
        ResponseEntity<GameResponseDTO> response = client.postForEntity("/games", gameToMake, GameResponseDTO.class);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(6)
    @Transactional
    public void testAddGameToCategory(){
        ResponseEntity response = client.exchange("/categories/" + TEST_CATEGORY_NAME2 + "/games/" + gameId, HttpMethod.PUT, null, ResponseEntity.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Category categoryFromDb = categoryRepo.findByName(TEST_CATEGORY_NAME2).get();
        Game gameFromDb = gameRepo.findById(gameId).get();
        assertTrue(gameFromDb.containsCategory(categoryFromDb));
        assertTrue(categoryFromDb.containsInCategory(gameFromDb));
    }

    @Test
    @Order(7)
    public void testAddGameToInvalidCategory(){
        ResponseEntity<Void> response = client.exchange("/categories/" + "INVALIDCATEGORY" + "/games/" + gameId, HttpMethod.PUT, null, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(8)
    @Transactional
    public void testRemoveGameFromCategory(){
        ResponseEntity response = client.exchange("/categories/" + TEST_CATEGORY_NAME2 + "/games/" + gameId, HttpMethod.DELETE, null, ResponseEntity.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Category categoryFromDb = categoryRepo.findByName(TEST_CATEGORY_NAME2).get();
        Game gameFromDb = gameRepo.findById(gameId).get();
        assertFalse(gameFromDb.containsCategory(categoryFromDb));
        assertFalse(categoryFromDb.containsInCategory(gameFromDb));
    }

    @Test
    @Order(9)
    public void testRemoveGameFromInvalidCategory(){
        ResponseEntity<Void> response = client.exchange("/categories/" + "INVALIDCATEGORY" + "/games/" + gameId, HttpMethod.DELETE, null, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(10)
    @Transactional
    public void testGetGamesInCategory(){
        ResponseEntity<List<GameResponseDTO>> response = client.exchange("/categories/" + TEST_CATEGORY_NAME1 + "/games", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        GameResponseDTO gameFromResponse = response.getBody().get(0);

        Game gameFromDb = gameRepo.findById(gameId).get();
        GameResponseDTO expectedDTO = new GameResponseDTO(gameFromDb);
        assertEquals(expectedDTO, gameFromResponse);
    }



    @Test
    @Order(11)
    @Transactional
    public void testDeleteCategory(){
        ResponseEntity response = client.exchange("/categories/" + TEST_CATEGORY_NAME1, HttpMethod.DELETE, null, ResponseEntity.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(categoryRepo.findByName(TEST_CATEGORY_NAME1).isPresent());

        //making sure game does not contain reference of deleted category
        Game gameFromDb = gameRepo.findById(gameId).get();
        assertTrue(gameFromDb.getCopyCategories().isEmpty());
    }

    @Test
    @Order(12)
    @Transactional
    public void testViewInventory(){
        ResponseEntity<List<GameResponseDTO>> response = client.exchange("/games", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        GameResponseDTO gameFromResponse = response.getBody().get(0);

        Game gameFromDb = gameRepo.findById(gameId).get();
        GameResponseDTO expectedDTO = new GameResponseDTO(gameFromDb);
        assertEquals(expectedDTO, gameFromResponse);
    }

    @Test
    @Order(13)
    @Transactional
    public void testUpdateStock(){
        int newStockNum = 40;
        ResponseEntity response = client.exchange("/games/" + gameId + "/stock?stockChange=" + newStockNum, HttpMethod.PUT, null, ResponseEntity.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Game gameFromDb = gameRepo.findById(gameId).get();
        assertEquals(newStockNum, gameFromDb.getStock());
    }

    @Test
    @Order(14)
    @Transactional
    public void testDeleteGame(){
        ResponseEntity response = client.exchange("/games/" + gameId, HttpMethod.DELETE, null, ResponseEntity.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Game gameFromDb = gameRepo.findById(gameId).get();
        assertFalse(gameFromDb.isAvailable());
    }
}
