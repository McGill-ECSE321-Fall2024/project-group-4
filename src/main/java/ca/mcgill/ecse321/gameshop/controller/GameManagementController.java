package ca.mcgill.ecse321.gameshop.controller;

import ca.mcgill.ecse321.gameshop.dto.GameRequestDTO;
import ca.mcgill.ecse321.gameshop.dto.GameResponseDTO;
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
    public List<GameResponseDTO> getGamesInCart(@PathVariable int customerId){
        Set<Game> gamesInCart = gameManagementService.viewGamesInCart(customerId);
        return gamesInCart.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }
    @PutMapping("customers/{customerId}/cart/{gameId}")
    public void addGameToCart(@PathVariable int customerId, @PathVariable int gameId){
        gameManagementService.addGameToCart(customerId, gameId);
    }

    @DeleteMapping("customers/{customerId}/cart/{gameId}")
    public void deleteGameFromCart(@PathVariable int customerId, @PathVariable int gameId){
        gameManagementService.removeGameFromCart(customerId, gameId);
    }

    @PostMapping("categories/{categoryName}")
    public void createCategory(@PathVariable String categoryName){
        gameManagementService.createCategory(categoryName);
    }

    @DeleteMapping("categories/{categoryName}")
    public void deleteCategory(@PathVariable String categoryName){
        gameManagementService.deleteCategory(categoryName);
    }

    @GetMapping("categories/{categoryName}/games")
    public List<GameResponseDTO> getGamesInCategory(@PathVariable String categoryName){
        Set<Game> gamesInCategory = gameManagementService.getGamesInCategory(categoryName);
        return gamesInCategory.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    @PutMapping("categories/{categoryName}/games/{gameId}")
    public void addGameToCategory(@PathVariable String categoryName, @PathVariable int gameId){
        gameManagementService.addGameToCategory(categoryName, gameId);
    }

    @DeleteMapping("categories/{categoryName}/games/{gameId}")
    public void removeGameFromCategory(@PathVariable String categoryName, @PathVariable int gameId){
        gameManagementService.removeGameFromCategory(categoryName, gameId);
    }


    /**
     * Endpoint to retrieve all games in the inventory.
     * @return A list of GameDTO objects representing all games in the inventory.
     */
    @GetMapping("games")
    public List<GameResponseDTO> getInventory() {
        Set<Game> inventory = gameManagementService.viewInventory();
        return inventory.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    @PostMapping("games")
    public GameResponseDTO addGame(@RequestBody GameRequestDTO gameRequestDTO) {
        Game newGame = gameManagementService.addNewGame(gameRequestDTO.name(), gameRequestDTO.description(),
                gameRequestDTO.coverPicture(), gameRequestDTO.price(), gameRequestDTO.isActive(),
                gameRequestDTO.stock(), gameRequestDTO.categories());

        return new GameResponseDTO(newGame);
    }


    @DeleteMapping("games/{gameId}")
    public void removeGame(@PathVariable int gameId) {
        gameManagementService.removeGame(gameId);
    }

    @PutMapping("games/{gameId}/stock")
    public void updateStock(@PathVariable int gameId, @RequestParam int stockChange) {
        gameManagementService.updateStock(gameId, stockChange);
    }
}
