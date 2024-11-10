package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class GameManagementService {
    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameRequestRepository gameRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void addGameToCart(int customerId, int gameId){
        Game gameToAdd = gameRepository.findById(gameId).orElseThrow(()-> new EntityNotFoundException("Game to add to cart not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new EntityNotFoundException("Customer to add game to cart of not found"));
        customer.addGameToCart(gameToAdd);
        customerRepository.save(customer);
    }

    public Set<Game> viewGamesInCart(int customerId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new EntityNotFoundException("Customer to add game to cart of not found"));
        return customer.getCopyCart();
    }
    @Transactional
    public void removeGameFromCart(int customerId, int gameId){
        Game gameToRemove = gameRepository.findById(gameId).orElseThrow(()-> new EntityNotFoundException("Game to remove from cart not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new EntityNotFoundException("Customer to remove game from cart of not found"));

        if(!customer.removeGameFromCart(gameToRemove)){
            throw new EntityNotFoundException("Game to remove was not in customer's cart");
        }
        customerRepository.save(customer);
    }
    @Transactional
    public void createCategory(String name){
        if(categoryRepo.findByName(name).isPresent()){
            throw new EntityExistsException("Category to add already exists");
        }
        Category category = new Category(name);
        categoryRepo.save(category);
    }
    @Transactional
    public void deleteCategory(String name){
        var category = categoryRepo.findByName(name).orElseThrow(()-> new EntityNotFoundException("Category to delete was not found"));
        categoryRepo.delete(category);
    }

    public Set<Game> getGamesInCategory(String name){
        var category = categoryRepo.findByName(name).orElseThrow(()-> new EntityNotFoundException("Category to get games in was not found"));
        return category.getCopyInCategory();

    }

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