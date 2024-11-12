package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
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

    @Autowired
    private PromotionRepository promotionRepository;

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

    /**
     * Get a game request from id
     *
     * @param id
     * @return GameRequest
     *
     * @author Camille Pouliot
     */
    @Transactional
    public GameRequest findGameRequestById(int id){
        Optional<GameRequest> gameRequest = gameRequestRepository.findById(id);
        if (gameRequest.isPresent()) {
            return gameRequest.get();
        }
        throw new IllegalArgumentException("No Game Request found");
    }

    /**
     * Get a game from id
     *
     * @param id
     * @return Game
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Game findGameById(int id){
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            return game.get();
        }
        throw new IllegalArgumentException("No Game found");
    }

    /**
     * Get an employee from id
     *
     * @param id
     * @return Employee
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Employee findEmployeeById(int id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        }
        throw new IllegalArgumentException("No Employee found");
    }

    /**
     * Create a game request
     *
     * @param externalReview
     * @param gameId
     * @param employeeId
     * @return GameRequest
     *
     * @author Camille Pouliot
     */
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

    /**
     * Approves a game request
     *
     * @param gameRequestId
     * @return GameRequest
     *
     * @author Camille Pouliot
     */
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

    /**
     * Rejects a game request
     *
     * @param gameRequestId
     * @return GameRequest
     *
     * @author Camille Pouliot
     */
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

    /**
     * Get a promotion from id
     *
     * @param id
     * @return Promotion
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Promotion findPromotionById(int id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isPresent()) {
            return promotion.get();
        }
        throw new IllegalArgumentException("Promotion not found");
    }

    /**
     * Create a promotion
     *
     * @param discount
     * @param startDate
     * @param endDate
     * @return Promotion
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Promotion createPromotion(int discount, Date startDate, Date endDate) {
        Promotion promotion = new Promotion(discount);

        if(discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        if(endDate.before(Date.valueOf(LocalDate.now()))) {
            throw new IllegalArgumentException("End date cannot be before current date");
        }
        if(startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);

        promotionRepository.save(promotion);
        return promotion;

    }

    /**
     * Delete a promotion
     *
     * @param id
     *
     * @author Camille Pouliot
     */
    @Transactional
    public void deletePromotion(int id) {

        Promotion promotion = findPromotionById(id);

        Set<Game> gamesInPromotion = promotion.getCopyGames();

        gamesInPromotion.forEach(game -> {
            promotion.removeGame(game);
            game.removePromotion(promotion);
            gameRepository.save(game);
        });

        promotionRepository.delete(promotion);
    }

    /**
     * Updates a promotion
     *
     * @param id
     * @param discount
     * @param startDate
     * @param endDate
     * @return Promotion
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Promotion updatePromotion(int id, int discount, Date startDate, Date endDate) {
        Promotion promotion = findPromotionById(id);

        if(discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        promotion.setDiscount(discount);
        if(endDate.before(Date.valueOf(LocalDate.now()))) {
            throw new IllegalArgumentException("End date cannot be before current date");
        }
        if(startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotionRepository.save(promotion);
        return promotion;

    }

    /**
     * Search games that corresponds to the search input
     *
     * @param searchInput
     * @return Set<Game>
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Set<Game> searchGames(String searchInput) {

        if (searchInput.isEmpty()) {
            Set<Game> games = new HashSet<>();
            gameRepository.findAll().forEach(game -> {if(game.isActive()){games.add(game);}});
            return games;
        }

        String[] words = searchInput.split(" ");

        Set<Game> searchedGames = new HashSet<>();

        gameRepository.findAll().forEach(game -> {

            if(game.isActive()) {

                int matchCounter = 0;
                String[] gameWords = game.getName().split(" ");

                for (String searchedWord : words) {
                    for (String word : gameWords) {
                        if (searchedWord.equalsIgnoreCase(word)) {
                            matchCounter++;
                            break;
                        }
                    }
                }
                if (matchCounter == words.length) {
                    searchedGames.add(game);
                }
            }
        });
        return searchedGames;
    }
}