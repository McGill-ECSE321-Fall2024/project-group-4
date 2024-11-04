package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRequestRepository;
import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.model.GameRequest;
import ca.mcgill.ecse321.gameshop.model.RequestStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Author : Tarek Namani
 */
@SpringBootTest
public class TestGameRequest {

    @Autowired
    private GameRequestRepository gameRequestRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private GameRepository gameRepostitory;

    private Game game = new Game("testame","testDescription","testCover",50.00F, true, 100);
    private Employee employee = new Employee("Bob","Joe",true);
    private String review = "Test review";
    private GameRequest gameRequest;


    @AfterEach
    public void clearRepository() {
        gameRequestRepository.deleteAll();
        gameRepostitory.deleteAll();
        employeeRepository.deleteAll();

    }

    @BeforeEach
    public void setup() {
        gameRequest = new GameRequest("test external review text", RequestStatus.PENDING, employee, game);
    }


    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading a GameRequest object from the database
     */
    @Test
    public void testSaveAndLoadGameRequest() {
        //save the gameRequest
        employeeRepository.save(employee);
        gameRepostitory.save(game);
        gameRequestRepository.save(gameRequest);

        //load the gameRequest
        GameRequest loadedRequest = gameRequestRepository.findById(gameRequest.getId()).orElse(null);

        //check the contents
        assertEquals(gameRequest.getGame().getName(),loadedRequest.getGame().getName());
        assertEquals(gameRequest.getRequestor().getUsername(),loadedRequest.getRequestor().getUsername());
        assertEquals(gameRequest.getStatus(),loadedRequest.getStatus());
        assertEquals(gameRequest.getExternalReview(),loadedRequest.getExternalReview());

    }


}
