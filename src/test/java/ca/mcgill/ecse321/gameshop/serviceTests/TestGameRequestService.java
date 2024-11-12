package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for game request service
 *
 * @author Camille Pouliot
 */
@SpringBootTest
public class TestGameRequestService {

    @InjectMocks
    private GameManagementService gameRequestService;

    @Mock
    private GameRequestRepository gameRequestRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    Game game;
    Game game2;
    Employee employee;
    GameRequest gameRequestApproved;
    GameRequest gameRequestDenied;
    GameRequest gameRequestPending;



    @AfterEach
    public void tearDown() {
        gameRequestRepository.deleteAll();
        gameRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        game = new Game("Game Name", "Game Description", "Game Picture", 10, false, 1);
        game2 = new Game("Other Game Name", "Other Game Description", "Other Game Picture", 10, true, 1);
        employee = new Employee("Employee Name", "Employee Password", true);
        gameRequestApproved = new GameRequest("External review approved", RequestStatus.APPROVED, employee, game);
        gameRequestDenied = new GameRequest("External review denied", RequestStatus.DENIED, employee, game);
        gameRequestPending = new GameRequest("External review pending", RequestStatus.PENDING, employee, game);

        when(gameRequestRepository.save(any(GameRequest.class))).thenReturn(gameRequestPending);
        when(gameRequestRepository.findById(1)).thenReturn(Optional.of(gameRequestPending));
        when(gameRequestRepository.findById(2)).thenReturn(Optional.of(gameRequestApproved));
        when(gameRequestRepository.findById(3)).thenReturn(Optional.of(gameRequestDenied));
        when(gameRequestRepository.findById(-1)).thenReturn(Optional.empty());


        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(gameRepository.findById(2)).thenReturn(Optional.of(game2));
        when(gameRepository.findById(-1)).thenReturn(Optional.empty());

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(-1)).thenReturn(Optional.empty());
    }

    /**
     * Test finding a game request
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindGameRequest() {
        //Act
        GameRequest loadedGameRequest = gameRequestService.findGameRequestById(1);

        //Assert
        assertNotNull(loadedGameRequest);
        assertEquals(loadedGameRequest.getExternalReview(), gameRequestPending.getExternalReview());
        assertEquals(loadedGameRequest.getStatus(), gameRequestPending.getStatus());
        assertEquals(loadedGameRequest.getRequestor(), gameRequestPending.getRequestor());
        assertEquals(loadedGameRequest, gameRequestPending);
        verify(gameRequestRepository, times(1)).findById(1);
    }

    /**
     * Test finding a game request with an invalid id
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindGameRequestInvalid() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.findGameRequestById(-1));

        //Assert
        assertEquals(exception.getMessage(), "No Game Request found");
        verify(gameRequestRepository, times(1)).findById(-1);
    }

    /**
     * Test finding a game
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindGame() {
        //Act
        Game loadedGame = gameRequestService.findGameById(1);

        //Assert
        assertNotNull(loadedGame);
        assertEquals(loadedGame.getName(), game.getName());
        assertEquals(loadedGame.getDescription(), game.getDescription());
        assertEquals(loadedGame.getCoverPicture(), game.getCoverPicture());
        assertEquals(loadedGame.getPrice(), game.getPrice());
        assertEquals(loadedGame.getStock(), game.getStock());
        assertEquals(loadedGame, game);
        verify(gameRepository, times(1)).findById(1);
    }

    /**
     * Test finding a game with an invalid id
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindGameInvalid() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.findGameById(-1));

        //Assert
        assertEquals(exception.getMessage(), "No Game found");
        verify(gameRepository, times(1)).findById(-1);
    }

    /**
     * Test finding an employee
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindEmployee() {
        //Act
        Employee loadedEmployee = gameRequestService.findEmployeeById(employee.getId());

        //Assert
        assertNotNull(loadedEmployee);
        assertEquals(loadedEmployee.getUsername(), employee.getUsername());
        assertEquals(loadedEmployee.getPassword(), employee.getPassword());
        assertEquals(loadedEmployee.isActive(), employee.isActive());
        assertEquals(loadedEmployee, employee);
        verify(employeeRepository, times(1)).findById(employee.getId());
    }

    /**
     * Test finding an employee with an invalid id
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindEmployeeInvalid() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.findEmployeeById(-1));

        //Assert
        assertEquals(exception.getMessage(), "No Employee found");
        verify(employeeRepository, times(1)).findById(-1);
    }

    /**
     * Test creating a game request
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreateGameRequestNormal() {
        //Act
            GameRequest loadedCreatedGameRequest = gameRequestService.createGameRequest("External Review", 1, employee.getId());

        //Assert
        assertNotNull(loadedCreatedGameRequest);
        assertEquals(loadedCreatedGameRequest.getRequestor().getId(), employee.getId());
        assertEquals(loadedCreatedGameRequest.getStatus(), RequestStatus.PENDING);
        assertEquals(loadedCreatedGameRequest.getExternalReview(), "External Review");
        verify(gameRequestRepository, times(1)).save(loadedCreatedGameRequest);

    }

    /**
     * Test creating a game request with an active game
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreateGameRequestActiveGame() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.createGameRequest("External Review", 2, employee.getId()));

        //Assert
        assertEquals(exception.getMessage(), "Game is already active");
    }

    /**
     * Test approving a game request
     *
     * @author Camille Pouliot
     */
    @Test
    public void testApproveGameRequestNormal() {
        //Act
        GameRequest loadedApprovedGameRequest = gameRequestService.approveGameRequest(1);

        //Assert
        assertNotNull(loadedApprovedGameRequest);
        assertEquals(loadedApprovedGameRequest.getGame().getId(), game.getId());
        assertEquals(loadedApprovedGameRequest.getRequestor().getId(), employee.getId());
        assertEquals(loadedApprovedGameRequest.getStatus(), RequestStatus.APPROVED);
        assertEquals(loadedApprovedGameRequest.getExternalReview(), "External review pending");
        verify(gameRequestRepository, times(1)).save(loadedApprovedGameRequest);
    }

    /**
     * Test approving a game request that was already approved
     *
     * @author Camille Pouliot
     */
    @Test
    public void testApproveGameRequestApprovedGameRequest() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.approveGameRequest(2));

        //Assert
        assertEquals(exception.getMessage(), "Game Request is already approved");
        verify(gameRequestRepository, times(1)).findById(2);
    }

    /**
     * Test approving a game request that was already rejected
     *
     * @author Camille Pouliot
     */
    @Test
    public void testApproveGameRequestDeniedGameRequest() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.approveGameRequest(3));

        //Assert
        assertEquals(exception.getMessage(), "Game Request is already denied");
        verify(gameRequestRepository, times(1)).findById(3);
    }

    /**
     * Test rejecting a game request
     *
     * @author Camille Pouliot
     */
    @Test
    public void testDenyGameRequestNormal() {
        //Act
        GameRequest loadedDeniedGameRequest = gameRequestService.rejectGameRequest(1);

        //Assert
        assertNotNull(loadedDeniedGameRequest);
        assertEquals(loadedDeniedGameRequest.getGame().getId(), game.getId());
        assertEquals(loadedDeniedGameRequest.getRequestor().getId(), employee.getId());
        assertEquals(loadedDeniedGameRequest.getStatus(), RequestStatus.DENIED);
        assertEquals(loadedDeniedGameRequest.getExternalReview(), "External review pending");
        verify(gameRequestRepository, times(1)).save(loadedDeniedGameRequest);
    }

    /**
     * Test rejecting a game request that was already approved
     *
     * @author Camille Pouliot
     */
    @Test
    public void testDenyGameRequestApprovedGameRequest() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.rejectGameRequest(2));

        //Assert
        assertEquals(exception.getMessage(), "Game Request is already approved");
        verify(gameRequestRepository, times(1)).findById(2);

    }

    /**
     * Test rejecting a game request that was already rejected
     *
     * @author Camille Pouliot
     */
    @Test
    public void testDenyGameRequestDeniedGameRequest() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameRequestService.rejectGameRequest(3));

        //Assert
        assertEquals(exception.getMessage(), "Game Request is already denied");
        verify(gameRequestRepository, times(1)).findById(3);
    }

}

