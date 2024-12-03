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
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Methods for GameManagementService
 *
 * @author Camille Pouliot,
 */
@Service
public class GameManagementService {
    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private GameRequestRepository gameRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    /**
     * Add a game to a customer cart
     *
     * @param customerId
     * @param gameId
     *
     * @author
     */
    @Transactional
    public void addGameToCart(int customerId, int gameId){
        Game gameToAdd = gameRepository.findById(gameId).orElseThrow(()-> new EntityNotFoundException("Game to add to cart not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new EntityNotFoundException("Customer to add game to cart of not found"));

        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartItemId_Customer_IdAndCartItemId_Game_Id(customerId, gameId);

        if(existingCartItemOpt.isPresent()){
            CartItem existingCartItem = existingCartItemOpt.get();
            if(existingCartItem.getQuantity() >= gameToAdd.getStock()){
                throw new IllegalArgumentException("Quantity in cart exceeds available inventory");
            }
            existingCartItem.setQuantity(existingCartItem.getQuantity()+1);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem(1, customer, gameToAdd);
            cartItemRepository.save(cartItem);
        }

    }

    /**
     * Get games in a customer cart
     *
     * @param customerId
     * @return Set<Game>
     *
     * @author
     */
    public Set<CartItem> viewGamesInCart(int customerId){
        if(!customerRepository.existsById(customerId)){
            throw new EntityNotFoundException("Customer to add game to cart of not found");
        }
        return cartItemRepository.findByCartItemId_Customer_Id(customerId);
    }

    /**
     * Remove a game from a customer cart
     *
     * @param customerId
     * @param gameId
     *
     * @author
     */
    @Transactional
    public void removeGameFromCart(int customerId, int gameId){
        if(!gameRepository.existsById(gameId) || !customerRepository.existsById(customerId)){
            throw new EntityNotFoundException("Game to remove from cart or customer not found");
        }

        CartItem existingCartItem = cartItemRepository.findByCartItemId_Customer_IdAndCartItemId_Game_Id(customerId, gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game to remove was not in customer's cart"));

        if(existingCartItem.getQuantity() > 1){
            existingCartItem.setQuantity(existingCartItem.getQuantity()-1);
            cartItemRepository.save(existingCartItem);
        } else{
            cartItemRepository.delete(existingCartItem);
        }
    }

    /**
     * Create a category
     *
     * @param name
     *
     * @author
     */
    @Transactional
    public void createCategory(String name){
        if(categoryRepo.findByName(name).isPresent()){
            throw new EntityExistsException("Category to add already exists");
        }
        Category category = new Category(name);
        categoryRepo.save(category);
    }

    /**
     * Delete a category
     *
     * @param name
     *
     * @author
     */
    @Transactional
    public void deleteCategory(String name){
        var category = categoryRepo.findByName(name).orElseThrow(()-> new EntityNotFoundException("Category to delete was not found"));

        for(Game game : category.getCopyInCategory()){
            if(!game.removeCategory(category)){
                throw new IllegalStateException("Could not remove games from category to delete");
            }
            gameRepository.save(game);
        }
        categoryRepo.delete(category);
    }

    /**
     * Get the games in a category
     *
     * @param name
     * @return Set<Game>
     *
     * @author
     */
    @Transactional
    public Set<Game> getGamesInCategory(String name){
        var category = categoryRepo.findByName(name).orElseThrow(()-> new EntityNotFoundException("Category to get games in was not found"));
        return category.getCopyInCategory();

    }

    /**
     * Add a game to a category
     *
     * @param categoryName
     * @param gameId
     *
     * @author
     */
    @Transactional
    public void addGameToCategory(String categoryName, int gameId){
        var category = categoryRepo.findByName(categoryName).orElseThrow(()-> new EntityNotFoundException("Category to add game to was not found"));
        var game = gameRepository.findById(gameId).orElseThrow(()-> new EntityNotFoundException("Game to add to category was not found"));

        if(!game.addCategory(category)){
            throw new IllegalArgumentException("Game already in category");
        }

        categoryRepo.save(category);
        gameRepository.save(game);
    }

    /**
     * Remove a game from a category
     *
     * @param categoryName
     * @param gameId
     *
     * @author
     */
    @Transactional
    public void removeGameFromCategory(String categoryName, int gameId){
        var category = categoryRepo.findByName(categoryName).orElseThrow(()-> new EntityNotFoundException("Category to remove game from was not found"));
        var game = gameRepository.findById(gameId).orElseThrow(()-> new EntityNotFoundException("Game to remove from category was not found"));

        if(!game.removeCategory(category)){
            throw new IllegalArgumentException("Game not in category");
        }

        categoryRepo.save(category);
        gameRepository.save(game);
    }

    /**
     * Get all games from inventory
     *
     * @return Set<Game>
     *
     * @author
     */
    public Set<Game> viewInventory() {
        // Convert Iterable<Game> to HashSet<Game>
        Iterable<Game> games = gameRepository.findAll();
        Set<Game> gameSet = new HashSet<>();
        games.forEach(gameSet::add);
        return gameSet;
    }

    /**
     * Add a new game
     *
     * @param name
     * @param description
     * @param cover
     * @param price
     * @param isActive
     * @param stock
     * @param categoryNames
     * @return Game
     *
     * @author
     */
    @Transactional
    public Game addNewGame(String name, String description, String cover, float price, boolean isActive, int stock, List<String> categoryNames) {
        // Create a new game
        Game newGame = new Game(name, description, cover, price, isActive, stock);
        gameRepository.save(newGame);

        // Find each category and associate it with the new game
        for (String categoryName : categoryNames) {
            var category = categoryRepo.findByName(categoryName)
                                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            category.addInCategory(newGame);
            categoryRepo.save(category);
        }

        return newGame;
    }

    /**
     * Remove a game (set to inactive)
     *
     * @param gameId
     *
     * @author
     */
    @Transactional
    public void removeGame(int gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        game.setAvailable(false);
        gameRepository.save(game);
    }

    /**
     * Update stock of a game
     *
     * @param gameId
     * @param newStock
     *
     * @author
     */
    @Transactional
    public void updateStock(int gameId, int newStock) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        if (newStock < 0) {
            throw new IllegalStateException("Cannot set stock to negative number");
        }
        game.setStock(newStock);
        gameRepository.save(game);
    }


    /**
     * @param gameId id of Game to update
     * @param is_active new state of the game
     * @throws EntityNotFoundException when the game does not exist
     * @author Tarek Namaani
     */
    @Transactional
    public void updateActivity(int gameId, boolean is_active) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        game.setActive(is_active);
        gameRepository.save(game);
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
        throw new EntityNotFoundException("No Game found");
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

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee does not exist"));

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

    /**
     * Add a promotion to a game and vice versa
     *
     * @param promotionId
     * @param gameId
     *
     * @author Camille Pouliot
     */
    @Transactional
    public void addPromotionToGame(int promotionId, int gameId) {
        Game game = findGameById(gameId);
        Promotion promotion = findPromotionById(promotionId);

        if(promotion.getCopyGames().contains(game) || game.containsPromotion(promotion)) {
            throw new IllegalArgumentException("Promotion already applied to game");
        }

        game.addPromotion(promotion);
        promotionRepository.save(promotion);
        gameRepository.save(game);
    }

    /**
     * Remove a promotion from a game and vice versa
     *
     * @param promotionId
     * @param gameId
     *
     * @author Camille Pouliot
     */
    @Transactional
    public void removePromotionFromGame(int promotionId, int gameId) {
        Game game = findGameById(gameId);
        Promotion promotion = findPromotionById(promotionId);

        if(!promotion.getCopyGames().contains(game) || !game.containsPromotion(promotion)) {
            throw new IllegalArgumentException("Promotion not applied to game");
        }

        game.removePromotion(promotion);
        promotionRepository.save(promotion);
        gameRepository.save(game);
    }

    @Transactional
    public Set<Promotion> getAllPromotions() {
        Set<Promotion> promotionSet = new HashSet<>();
        if (promotionRepository.findAll().iterator().hasNext()) {
            promotionRepository.findAll().forEach(promotionSet::add);
        }
        return promotionSet;
    }

    


}
    