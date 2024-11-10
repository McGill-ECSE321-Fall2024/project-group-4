package ca.mcgill.ecse321.gameshop.controller;

import ca.mcgill.ecse321.gameshop.dto.GameDTO;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class GameManagementController {
    @Autowired
    private GameManagementService gameManagementService;

    @GetMapping("customers/{customerId}/cart")
    public List<GameDTO> getGamesInCart(@PathVariable int customerId){
        Set<Game> gamesInCart = gameManagementService.viewGamesInCart(customerId);
        return gamesInCart.stream().map(GameDTO::new).collect(Collectors.toList());
    }
    @PutMapping("customers/{customerId}/cart/{gameId}")
    public void addGameToCart(@PathVariable int customerId, @PathVariable int gameId){
        gameManagementService.addGameToCart(customerId, gameId);
    }

    @DeleteMapping("customers/{customerId}/cart/{gameId}")
    public void deleteGameFromCart(@PathVariable int customerId, @PathVariable int gameId){
        gameManagementService.removeGameFromCart(customerId, gameId);
    }

    @GetMapping("categories/{categoryName}/games")
    public List<GameDTO> getGamesInCategory(@PathVariable String categoryName){
        Set<Game> gamesInCategory = gameManagementService.getGamesInCategory(categoryName);
        return gamesInCategory.stream().map(GameDTO::new).collect(Collectors.toList());
    }

    @PostMapping("categories/{categoryName}")
    public void createCategory(@PathVariable String categoryName){
        gameManagementService.createCategory(categoryName);
    }

    @DeleteMapping("categories/{categoryName}")
    public void deleteCategory(@PathVariable String categoryName){
        gameManagementService.deleteCategory(categoryName);
    }

    /**
     * Endpoint to retrieve all games in the inventory.
     * @return A list of GameDTO objects representing all games in the inventory.
     */
    @GetMapping("games")
    public List<GameDTO> getInventory() {
        Set<Game> inventory = gameManagementService.viewInventory();
        return inventory.stream().map(GameDTO::new).collect(Collectors.toList());
    }

    @PostMapping("games")
    public GameDTO addGame(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String cover,
            @RequestParam float price,
            @RequestParam boolean isActive,
            @RequestParam int stock,
            @RequestParam List<String> categories) {
        Game newGame = gameManagementService.addNewGame(name, description, cover, price, isActive, stock, categories);
        return new GameDTO(newGame);
    }


    @DeleteMapping("games/{gameId}")
    public void removeGame(@PathVariable int gameId) {
        gameManagementService.removeGame(gameId);
    }

    @PutMapping("games/{gameId}/stock")
    public GameDTO updateStock(@PathVariable int gameId, @RequestParam int stockChange) {
        gameManagementService.updateStock(gameId, stockChange);
        Game updatedGame = gameManagementService.viewInventory()
                            .stream()
                            .filter(game -> game.getId() == gameId)
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("Game not found"));
        return new GameDTO(updatedGame);
    }


}
