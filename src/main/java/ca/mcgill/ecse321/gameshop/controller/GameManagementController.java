package ca.mcgill.ecse321.gameshop.controller;

import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.CartItem;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.model.GameRequest;
import ca.mcgill.ecse321.gameshop.model.Promotion;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Methods of game management controller
 *
 * @author Jake Kogut, Clara Mickail, Camille Pouliot
 */
@RestController
public class GameManagementController {
    @Autowired
    private GameManagementService gameManagementService;

    /**
     * Get the list of games in a customer cart
     *
     * @param customerId Customer unique identifier
     * @return a list of the games in the customer cart
     *
     * @author Jake Kogut
     */
    @GetMapping("/customers/{customerId}/cart")
    public List<CartItemResponseDTO> getGamesInCart(@PathVariable int customerId){
        Set<CartItem> gamesInCart = gameManagementService.viewGamesInCart(customerId);
        return gamesInCart.stream().map(CartItemResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * Add a game to a customer cart
     *
     * @param customerId Customer unique identifier
     * @param gameId Unique identifier to add to the customer cart
     *
     * @author Jake Kogut
     */
    @PutMapping("/customers/{customerId}/cart/{gameId}")
    public void addGameToCart(@PathVariable int customerId, @PathVariable int gameId){
        gameManagementService.addGameToCart(customerId, gameId);
    }

    /**
     * Delete a game from a customer cart
     *
     * @param customerId Customer unique identifier
     * @param gameId Game unique identifier to delete
     *
     * @author Jake Kogut
     */
    @DeleteMapping("/customers/{customerId}/cart/{gameId}")
    public void deleteGameFromCart(@PathVariable int customerId, @PathVariable int gameId){
        gameManagementService.removeGameFromCart(customerId, gameId);
    }

    /**
     * Create a category
     *
     * @param categoryName Name of the category to create
     *
     * @author Jake Kogut
     */
    @PostMapping("/categories/{categoryName}")
    public void createCategory(@PathVariable String categoryName){
        gameManagementService.createCategory(categoryName);
    }

    /**
     * Delete a category
     *
     * @param categoryName Name of the category to delete
     *
     * @author Jake Kogut
     */
    @DeleteMapping("/categories/{categoryName}")
    public void deleteCategory(@PathVariable String categoryName){
        gameManagementService.deleteCategory(categoryName);
    }

    /**
     * Get a list of games from a category
     *
     * @param categoryName Name of a category
     * @return List of the games in the category
     *
     * @author Jake Kogut
     */
    @GetMapping("/categories/{categoryName}/games")
    public List<GameResponseDTO> getGamesInCategory(@PathVariable String categoryName){
        Set<Game> gamesInCategory = gameManagementService.getGamesInCategory(categoryName);
        return gamesInCategory.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * Add a game to a category
     *
     * @param categoryName Name of a category
     * @param gameId Game unique identifier to add to the category
     *
     * @author Jake Kogut
     */
    @PutMapping("/categories/{categoryName}/games/{gameId}")
    public void addGameToCategory(@PathVariable String categoryName, @PathVariable int gameId){
        gameManagementService.addGameToCategory(categoryName, gameId);
    }

    /**
     * Delete a game from a category
     *
     * @param categoryName Name of a category
     * @param gameId Game unique identifier to remove from the category
     *
     * @author Jake Kogut
     */
    @DeleteMapping("/categories/{categoryName}/games/{gameId}")
    public void removeGameFromCategory(@PathVariable String categoryName, @PathVariable int gameId){
        gameManagementService.removeGameFromCategory(categoryName, gameId);
    }


    /**
     * Endpoint to retrieve all games in the inventory.
     *
     * @return A list of GameResponseDTO objects representing all games in the inventory.
     *
     * @author Clara Mickail
     */
    @GetMapping("/games")
    public List<GameResponseDTO> getInventory() {
        Set<Game> inventory = gameManagementService.viewInventory();
        return inventory.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/games")
    public GameResponseDTO addGame(@RequestBody GameInputDTO gameRequestDTO) {
        Game newGame = gameManagementService.addNewGame(gameRequestDTO.name(), gameRequestDTO.description(),
                gameRequestDTO.coverPicture(), gameRequestDTO.price(), gameRequestDTO.isActive(),
                gameRequestDTO.stock(), gameRequestDTO.categories());

        return new GameResponseDTO(newGame);
    }

    /**
     * Delete a game
     *
     * @param gameId Game unique identifier to delete
     *
     * @author Clara Mickail
     */
    @DeleteMapping("/games/{gameId}")
    public void removeGame(@PathVariable int gameId) {
        gameManagementService.removeGame(gameId);
    }

    /**
     * Update the stock of a game
     *
     * @param gameId Game unique identifier to update
     * @param stockChange New stock of the game
     *
     * @author Clara Mickail
     */
    @PutMapping("/games/{gameId}/stock/{stockChange}")
    public void updateStock(@PathVariable int gameId, @PathVariable int stockChange) {
        gameManagementService.updateStock(gameId, stockChange);
    }


    /**
     * Return the game with the given id
     *
     * @param gameId  Game unique identifier
     * @return Game DTO corresponding to the id
     *
     * @author Camille Pouliot
     */
    @GetMapping("/games/{gameId}")
    public GameResponseDTO getGameById(@PathVariable int gameId) {
        return new GameResponseDTO(gameManagementService.findGameById(gameId));
    }

    @GetMapping("/games/categories/{category}")
    public List<GameResponseDTO> getGameByCategory(@PathVariable String category) {
        return gameManagementService.getGameByCategory(category).stream().map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }


    /**
     * Return the games that contains the search prompt
     *
     * @param searchPrompt String of words to search in the catalogue
     * @return List of the games that correspond to the search prompt
     *
     * @author Camille Pouliot
     */
    @GetMapping("catalogue/games/{searchPrompt}")
    public List<GameResponseDTO> getGamesBySearchPrompt(@PathVariable String searchPrompt) {
        Set<Game> searchedGames = gameManagementService.searchGames(searchPrompt);
        return searchedGames.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * Return the game request with the given id
     *
     * @param gameRequestId Game Request identifier
     * @return Game request DTO corresponding to the id
     *
     * @author Camille Pouliot
     */
    @GetMapping("/gameRequests/{gameRequestId}")
    public GameRequestDTO getGameRequestById(@PathVariable int gameRequestId) {
        return new GameRequestDTO(gameManagementService.findGameRequestById(gameRequestId));
    }

    /**
     * Create a new game request
     *
     * @param gameRequestDTO Game request DTO to add to the server
     * @return Game request DTO of the created game request, different from the param
     *
     * @author Camille Pouliot
     */
    @PostMapping("/gameRequests")
    public GameRequestDTO createGameRequest(@RequestBody GameRequestDTO gameRequestDTO){
        GameRequest createdGameRequest = gameManagementService.createGameRequest(gameRequestDTO.externalReview(), gameRequestDTO.game().id(), gameRequestDTO.requestor().id());
        return new GameRequestDTO(createdGameRequest);
    }

    @GetMapping("/gameRequests")
    public Set<GameRequestDTO> getEmployees() {
        return gameManagementService.getAllGameRequests().stream().map(GameRequestDTO::new).collect(Collectors.toSet());
    }

    /**
     * Set the status of a pending game request
     *
     * @param gameRequestId Game request unique identifier
     * @param status New status of the pending game request (approve, reject)
     *
     * @author Camille Pouliot
     */
    @PutMapping("/gameRequests/{gameRequestId}/approve")
    public String approveGameRequest(@PathVariable int gameRequestId){
        gameManagementService.approveGameRequest(gameRequestId);
        return "Approved";
    }

    @PutMapping("/gameRequests/{gameRequestId}/deny")
    public String denyGameRequest(@PathVariable int gameRequestId){
        gameManagementService.rejectGameRequest(gameRequestId);
        return "Rejected";
    }

    /**
     * Return the promotion with the given id
     *
     * @param promotionId Promotion unique identifier
     * @return Promotion DTO corresponding to the unique identifier
     *
     * @author Camille Pouliot
     */
    @GetMapping("/promotions/{promotionId}")
    public PromotionResponseDTO getPromotionById(@PathVariable int promotionId) {
        return new PromotionResponseDTO(gameManagementService.findPromotionById(promotionId));
    }

    /**
     * Create a new promotion
     *
     * @param promotionRequestDTO Promotion DTO to add to the server
     * @return Promotion DTO of the created promotion, different from the param
     *
     * @author Camille Pouliot
     */
    @PostMapping("/promotions")
    public PromotionResponseDTO createPromotion(@RequestBody PromotionRequestDTO promotionRequestDTO){
        Promotion createdPromotion = gameManagementService.createPromotion(promotionRequestDTO.discount(), promotionRequestDTO.startDate(), promotionRequestDTO.endDate());
        return new PromotionResponseDTO(createdPromotion);
    }



    /**
     * Update a promotion fields
     *
     * @param promotionId Promotion unique identifier
     *
     * @author Camille Pouliot
     */
    @PostMapping("/promotions/{promotionId}")
    public void updatePromotion(@PathVariable int promotionId, @RequestBody PromotionRequestDTO promotionDTO){
        gameManagementService.updatePromotion(promotionId, promotionDTO.discount(), promotionDTO.startDate(), promotionDTO.endDate());
    }

    /**
     * Delete a promotion
     *
     * @param promotionId Promotion unique identifier to delete
     *
     * @author Camille Pouliot
     */
    @DeleteMapping("/promotions/{promotionId}")
    public void deletePromotion(@PathVariable int promotionId){
        gameManagementService.deletePromotion(promotionId);
    }

    /**
     * Add a promotion to a game
     *
     * @param gameId
     * @param promotionId
     *
     * @author Camille Pouliot
     */
    @PutMapping("/games/{gameId}/promotions/{promotionId}")
    public void addPromotionToGame(@PathVariable int gameId, @PathVariable int promotionId) {
        gameManagementService.addPromotionToGame(promotionId,gameId);
    }

    /**
     * Remove a promotion from a game
     *
     * @param gameId
     * @param promotionId
     *
     * @author Camille Pouliot
     */
    @DeleteMapping("/games/{gameId}/promotions/{promotionId}")
    public void removePromotionFromGame(@PathVariable int gameId, @PathVariable int promotionId) {
        gameManagementService.removePromotionFromGame(promotionId, gameId);
    }


    @PutMapping("/games/{gameId}/active/{is_active}")
    public void setGameActivity(@PathVariable boolean is_active, @PathVariable int gameId) {
        gameManagementService.updateActivity(gameId, is_active);
    }

    @GetMapping("/promotions")
    public Set<PromotionResponseDTO> getAllPromotions() {
        Set<PromotionResponseDTO> promotions = new HashSet<>();
        gameManagementService.getAllPromotions().forEach(promotion -> promotions.add(new PromotionResponseDTO(promotion)));
        return promotions;
    }

    @GetMapping("/categories")
    public Set<CategoryResponseDTO> getAllCategories() {
        Set<CategoryResponseDTO> categories = new HashSet<>();
        gameManagementService.getAllCategories().forEach(category -> categories.add(new CategoryResponseDTO(category)));
        return categories;
    }

    @GetMapping("/promotions/{promotionId}/games")
    public Set<GameResponseDTO> getAllGamesInCategory(@PathVariable int promotionId) {
        Set<GameResponseDTO> games = new HashSet<>();
        gameManagementService.getGamesForPromotion(promotionId).forEach(gameResponse -> games.add(new GameResponseDTO(gameResponse)));
        return games;
    }
}
