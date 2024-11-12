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

    /**
     * Return the game request with the given id
     *
     * @param gameRequestId
     * @return GameRequestDTO
     *
     * @author Camille Pouliot
     */
    @GetMapping("/gameRequests/{gameRequestId}")
    public GameRequestDTO getGameRequestById(@PathVariable int gameRequestId) {
        return new GameRequestDTO(gameManagementService.findGameRequestById(gameRequestId));
    }

    /**
     * Return the game with the given id
     *
     * @param gameId
     * @return GameDTO
     *
     * @author Camille Pouliot
     */
    @GetMapping("/games/{gameId}")
    public GameDTO getGameById(@PathVariable int gameId) {
        return new GameDTO(gameManagementService.findGameById(gameId));
    }

    /**
     * Return the employee with the given id
     *
     * @param employeeId
     * @return EmployeeDTO
     *
     * @author Camille Pouliot
     */
    @GetMapping("/employees/{employeeId}")
    public EmployeeDTO getEmployeeById(@PathVariable int employeeId) {
        return new EmployeeDTO(gameManagementService.findEmployeeById(employeeId));
    }

    /**
     * Return the promotion with the given id
     *
     * @param promotionId
     * @return PromotionDTO
     *
     * @author Camille Pouliot
     */
    @GetMapping("/promotions/{promotionId}")
    public PromotionDTO getPromotionById(@PathVariable int promotionId) {
        return new PromotionDTO(gameManagementService.findPromotionById(promotionId));
    }

    /**
     * Return the games that contains the search prompt
     *
     * @param searchPrompt
     * @return Set<GameDTO>
     *
     * @author Camille Pouliot
     */
    @GetMapping("/games/{searchPrompt}")
    public Set<GameDTO> getGamesBySearchPrompt(@PathVariable String searchPrompt) {
        Set<Game> searchedGames = gameManagementService.searchGames(searchPrompt);
        return searchedGames.stream().map(GameDTO::new).collect(Collectors.toSet());
    }

    /**
     * Create a new game request
     *
     * @param gameRequestDTO
     * @return GameRequestDTO
     *
     * @author Camille Pouliot
     */
    @PostMapping("/gameRequests")
    public GameRequestDTO createGameRequest(@RequestBody GameRequestDTO gameRequestDTO){
        GameRequest createdGameRequest = gameManagementService.createGameRequest(gameRequestDTO.externalReview(), gameRequestDTO.game().id(), gameRequestDTO.requestor().id());
        return new GameRequestDTO(createdGameRequest);
    }

    /**
     * Create a new promotion
     *
     * @param promotionDTO
     * @param startDate
     * @param endDate
     * @return PromotionDTO
     *
     * @author Camille Pouliot
     */
    @PostMapping("/promotions")
    public PromotionDTO createPromotion(@RequestBody PromotionDTO promotionDTO, @RequestParam Date startDate, @RequestParam Date endDate){
        Promotion createdPromotion = gameManagementService.createPromotion(promotionDTO.discount(), startDate, endDate);
        return new PromotionDTO(createdPromotion);
    }

    /**
     * Set the status of a pending game request
     *
     * @param gameRequestId
     * @param status
     *
     * @author Camille Pouliot
     */
    @PutMapping("/gameRequests/{gameRequestId}/requestStatus/{status}")
    public void setGameRequestStatus(@PathVariable int gameRequestId, @PathVariable String status){
        if(status.equalsIgnoreCase("approve")){
            gameManagementService.approveGameRequest(gameRequestId);
        } else if(status.equalsIgnoreCase("reject")){
            gameManagementService.rejectGameRequest(gameRequestId);
        }
    }

    /**
     * Update a promotion fields
     *
     * @param promotionId
     * @param discount
     * @param startDate
     * @param endDate
     *
     * @author Camille Pouliot
     */
    @PutMapping("/promotions/{promotionId}/{discount}")
    public void updatePromotion(@PathVariable int promotionId, @PathVariable int discount, @RequestParam Date startDate, @RequestParam Date endDate){
        gameManagementService.updatePromotion(promotionId, discount, startDate, endDate);
    }

    /**
     * Delete a promotion
     *
     * @param promotionId
     *
     * @author Camille Pouliot
     */
    @DeleteMapping("/promorions/{promotionId}")
    public void deletePromotion(@PathVariable int promotionId){
        gameManagementService.deletePromotion(promotionId);
    }

}
