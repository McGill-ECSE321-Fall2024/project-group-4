package ca.mcgill.ecse321.gameshop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.*;

import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestGameRequestIntegration {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameRequestRepository gameRequestRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GameManagementService gameManagementService;

    //Category
    private String categoryName = "category";
    private Category category = new Category(categoryName);

    //Active Game 1
    private int game1Id;
    private final String game1Name = "Game Name";
    private final String game1Description = "Game Description";
    private final String game1Cover = "Game Cover";
    private final int game1Price = 10;
    private Boolean game1Active = true;
    private final int game1Stock = 1;
    private Game game1 = new Game(game1Name, game1Description, game1Cover, game1Price, game1Active, game1Stock);


    //Active Game 2
    private int game2Id;
    private String game2Name = "Other Game Name";
    private String game2Description = "Other Game Description";
    private String game2Cover = "Other Game Cover";
    private int game2Price = 10;
    private Boolean game2Active = false;
    private int game2Stock = 1;
    private GameResponseDTO game2DTO;

    //Customer
    private final String customerUsername = "customer";
    private final String customerEmail = "customer@email.com";
    private final String customerPassword = "password";
    private final String customerPhoneNumber = "123456789";
    private Customer customer = new Customer(customerUsername, customerEmail, customerPassword, customerPhoneNumber);

    //Employee
    private final String employeeUsername = "employee";
    private final String employeePassword = "password";
    private final Boolean employeeActive = true;
    private final Employee employee = new Employee(employeeUsername, employeePassword, employeeActive);
    private EmployeeDTO employeeDTO = new EmployeeDTO(employee);

    //Approved Game Request
    private int approvedGameRequestId;
    private final String approvedGameRequestExternalReview = "Game Request Approved";
    private final RequestStatus approvedGameRequestStatus = RequestStatus.APPROVED;
    private final Game approvedGameRequestGame = game1;
    private final Employee approvedGameRequestRequester = employee;
    private GameRequest approvedGameRequest = new GameRequest(approvedGameRequestExternalReview, approvedGameRequestStatus, approvedGameRequestRequester, approvedGameRequestGame);

    //Create Game Request
    private String createGameRequestExternalReview = "Game Request Created";
    private RequestStatus createGameRequestStatus = RequestStatus.PENDING;
    private GameRequestDTO gameRequestDTO;
    private int createGameRequestId;

    @BeforeAll
    public void setup() {
        category.addInCategory(game1);
        gameRepository.save(game1);
        categoryRepository.save(category);
        customerRepository.save(customer);
        employeeRepository.save(employee);
        gameRequestRepository.save(approvedGameRequest);
    }

    @AfterAll
    public void tearDown() {
        gameRequestRepository.deleteAll();
        categoryRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();

    }

    @Test
    @Order(1)
    public void createGameTestGameRequest(){
        //Arrange
        Game newGame = new Game(game2Name, game2Description, game2Cover, game2Price, game2Active, game2Stock);
        GameResponseDTO gameDTO = new GameResponseDTO(newGame);

        //Act
        ResponseEntity<GameResponseDTO> response = restTemplate.postForEntity("/games", gameDTO, GameResponseDTO.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(game2Name, responseDTO.name());
        assertEquals(game2Description, responseDTO.description());
        assertEquals(game2Cover, responseDTO.coverPicture());
        assertEquals(game2Price, responseDTO.price());
        assertEquals(game2Active, responseDTO.isActive());
        assertEquals(game2Stock, responseDTO.stock());
        assertEquals(   2, gameRepository.count());

        this.game2Id = responseDTO.id();
        this.game2DTO = responseDTO;
    }

    @Test
    @Order(2)
    public void createGameRequestTest(){
        //Arrange
        GameRequest newGameRequest = new GameRequest(createGameRequestExternalReview, createGameRequestStatus, this.employee, gameManagementService.findGameById(this.game2Id));
        GameRequestDTO gameRequestDTO = new GameRequestDTO(newGameRequest);

        //Act
        ResponseEntity<GameRequestDTO> response = restTemplate.postForEntity("/gameRequests", gameRequestDTO, GameRequestDTO.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestDTO responseGameRequestDTO = response.getBody();
        assertNotNull(responseGameRequestDTO);
        assertEquals(createGameRequestExternalReview, responseGameRequestDTO.externalReview());
        assertEquals(createGameRequestStatus, responseGameRequestDTO.requestStatus());
        assertEquals(this.game2Id, responseGameRequestDTO.game().id());
        assertEquals(this.employee.getId(), responseGameRequestDTO.requestor().id());

        this.createGameRequestId = responseGameRequestDTO.id();
        this.gameRequestDTO = responseGameRequestDTO;

    }

    @Test
    @Order(3)
    public void createGameRequestActiveGameTest(){
        //Arrange
        GameRequestDTO gameRequestDTO1 = new GameRequestDTO(2, createGameRequestExternalReview, createGameRequestStatus, this.employeeDTO, new GameResponseDTO(game1));

        //Act
        ResponseEntity<GameRequestDTO> response = restTemplate.postForEntity("/gameRequests", gameRequestDTO1, GameRequestDTO.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void searchGamesTest(){
        //Arrange
        String searchPrompt = "Game";

        //Act
        ResponseEntity<Set<GameResponseDTO>> response = restTemplate.getForEntity("/catalogue/games/" + searchPrompt, null, Set.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Set<GameResponseDTO> responseDTOSet = response.getBody();
        assertNotNull(responseDTOSet);
        assertEquals(1, responseDTOSet.size());
        GameResponseDTO responseDTO = responseDTOSet.iterator().next();
        assertNotNull(responseDTO);
        assertEquals(game1.getName(), responseDTO.name());
        assertEquals(game1.getDescription(), responseDTO.description());
        assertEquals(game1.getCoverPicture(), responseDTO.coverPicture());
        assertEquals(game1.getPrice(), responseDTO.price());
        assertEquals(game1.isActive(), responseDTO.isActive());
        assertEquals(game1.getStock(), responseDTO.stock());

    }

    @Test
    @Order(5)
    public void approveGameRequestTest(){
        //Act
        ResponseEntity<String> response = restTemplate.getForEntity("/gameRequests/" + this.createGameRequestId + "/approve", null, String.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String approveResponse = response.getBody();
        assertNotNull(approveResponse);
        assertTrue(approveResponse.contains("Approved"));
        assertTrue(gameRepository.findById(this.game2Id).get().isActive());


    }

    @Test
    @Order(6)
    public void searchGamesTest2() {
        //Arrange
        String searchPrompt = "Game";

        //Act
        ResponseEntity<Set<GameResponseDTO>> response = restTemplate.getForEntity("/catalogue/games/" + searchPrompt, null, String.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Set<GameResponseDTO> responseDTOSet = response.getBody();
        assertNotNull(responseDTOSet);
        assertEquals(2, responseDTOSet.size());
        GameResponseDTO responseDTO = responseDTOSet.iterator().next();
        assertNotNull(responseDTO);
        GameResponseDTO responseDTO2 = responseDTOSet.iterator().next();
        assertNotNull(responseDTO2);
    }



}
