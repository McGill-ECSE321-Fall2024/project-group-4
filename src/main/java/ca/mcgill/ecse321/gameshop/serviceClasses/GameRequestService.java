package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameRequestService {

    @Autowired
    private GameRequestRepository gameRequestRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public GameRequest findGameRequestById(int id){
        Optional<GameRequest> gameRequest = gameRequestRepository.findById(id);
        if (gameRequest.isPresent()) {
            return gameRequest.get();
        }
        throw new IllegalArgumentException("No Game Request found");
    }

    @Transactional
    public Game findGameById(int id){
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            return game.get();
        }
        throw new IllegalArgumentException("No Game found");
    }

    @Transactional
    public Employee findEmployeeById(int id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        }
        throw new IllegalArgumentException("No Employee found");
    }

    @Transactional
    public GameRequest createGameRequest(String externalReview, int gameId, int employeeId){

        Game game = findGameById(gameId);

        if(game.isActive()) {
            throw new IllegalArgumentException("Game is already active");
        }

        Employee employee = findEmployeeById(employeeId);

        GameRequest gameRequest = new GameRequest(externalReview, RequestStatus.PENDING, employee, game);

        employee.addGameRequest(gameRequest);

        gameRequestRepository.save(gameRequest);
        employeeRepository.save(employee);

        return gameRequest;

    }

    @Transactional
    public GameRequest approveGameRequest(int gameRequestId){
        GameRequest gameRequest = findGameRequestById(gameRequestId);

        if(gameRequest.getStatus() == RequestStatus.APPROVED){
            throw new IllegalArgumentException("Game Request is already approved");
        }
        if(gameRequest.getStatus() == RequestStatus.DENIED){
            throw new IllegalArgumentException("Game Request is already denied");
        }

        Game game = gameRequest.getGame();
        game.setActive(true);
        gameRepository.save(game);
        gameRequest.setStatus(RequestStatus.APPROVED);
        gameRequestRepository.save(gameRequest);
        return gameRequest;
    }

    @Transactional
    public GameRequest rejectGameRequest(int gameRequestId){
        GameRequest gameRequest = findGameRequestById(gameRequestId);

        if(gameRequest.getStatus() == RequestStatus.APPROVED){
            throw new IllegalArgumentException("Game Request is already approved");
        }
        if(gameRequest.getStatus() == RequestStatus.DENIED){
            throw new IllegalArgumentException("Game Request is already denied");
        }

        gameRequest.setStatus(RequestStatus.DENIED);
        gameRequestRepository.save(gameRequest);
        return gameRequest;
    }


}
