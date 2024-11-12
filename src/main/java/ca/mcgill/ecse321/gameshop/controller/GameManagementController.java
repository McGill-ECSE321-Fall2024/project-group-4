package ca.mcgill.ecse321.gameshop.controller;

import ca.mcgill.ecse321.gameshop.dto.EmployeeDTO;
import ca.mcgill.ecse321.gameshop.dto.GameDTO;
import ca.mcgill.ecse321.gameshop.dto.GameRequestDTO;
import ca.mcgill.ecse321.gameshop.dto.PromotionDTO;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.model.GameRequest;
import ca.mcgill.ecse321.gameshop.model.Promotion;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class GameManagementController {
    @Autowired
    private GameManagementService gameManagementService;

    @GetMapping("carts/{customerId}")
    public List<GameDTO> getGamesInCart(@PathVariable int customerId){
        Set<Game> gamesInCart = gameManagementService.viewGamesInCart(customerId);
        return gamesInCart.stream().map(GameDTO::new).collect(Collectors.toList());
    }
    @PutMapping("carts/{customerId}/{gameId}")
    public void addGameToCart(@PathVariable int customerId, @PathVariable int gameId){
        gameManagementService.addGameToCart(customerId, gameId);
    }

    @DeleteMapping("carts/{customerId}/{gameId}")
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

    @GetMapping("/gameRequests/{gameRequestId}")
    public GameRequestDTO getGameRequestById(@PathVariable int gameRequestId) {
        return new GameRequestDTO(gameManagementService.findGameRequestById(gameRequestId));
    }

    @GetMapping("/games/{gameId}")
    public GameDTO getGameById(@PathVariable int gameId) {
        return new GameDTO(gameManagementService.findGameById(gameId));
    }

    @GetMapping("/employees/{employeeId}")
    public EmployeeDTO getEmployeeById(@PathVariable int employeeId) {
        return new EmployeeDTO(gameManagementService.findEmployeeById(employeeId));
    }

    @GetMapping("/promotions/{promotionId}")
    public PromotionDTO getPromotionById(@PathVariable int promotionId) {
        return new PromotionDTO(gameManagementService.findPromotionById(promotionId));
    }

    @GetMapping("/games/{searchPrompt}")
    public Set<GameDTO> getGamesBySearchPrompt(@PathVariable String searchPrompt) {
        Set<Game> searchedGames = gameManagementService.searchGames(searchPrompt);
        return searchedGames.stream().map(GameDTO::new).collect(Collectors.toSet());
    }

    @PostMapping("/gameRequests")
    public GameRequestDTO createGameRequest(@RequestBody GameRequestDTO gameRequestDTO){
        GameRequest createdGameRequest = gameManagementService.createGameRequest(gameRequestDTO.externalReview(), gameRequestDTO.game().id(), gameRequestDTO.requestor().id());
        return new GameRequestDTO(createdGameRequest);
    }

    @PostMapping("/promotions")
    public PromotionDTO createPromotion(@RequestBody PromotionDTO promotionDTO, @RequestParam Date startDate, @RequestParam Date endDate){
        Promotion createdPromotion = gameManagementService.createPromotion(promotionDTO.discount(), startDate, endDate);
        return new PromotionDTO(createdPromotion);
    }

    @PutMapping("/gameRequests/{gameRequestId}/requestStatus/{status}")
    public void setGameRequestStatus(@PathVariable int gameRequestId, @PathVariable String status){
        if(status.equalsIgnoreCase("approve")){
            gameManagementService.approveGameRequest(gameRequestId);
        } else if(status.equalsIgnoreCase("reject")){
            gameManagementService.rejectGameRequest(gameRequestId);
        }
    }

    @PutMapping("/promotions/{promotionId}/{discount}")
    public void updatePromotionDiscount(@PathVariable int promotionId, @PathVariable int discount, @RequestParam Date startDate, @RequestParam Date endDate){
        gameManagementService.updatePromotion(promotionId, discount, startDate, endDate);
    }

    @DeleteMapping("/promorions/{promotionId}")
    public void deletePromotion(@PathVariable int promotionId){
        gameManagementService.deletePromotion(promotionId);
    }

}
