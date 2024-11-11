package ca.mcgill.ecse321.gameshop.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gameshop.dto.GameDTO;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

@RestController
@RequestMapping("/accounts")
public class AccountManagementController {

    @Autowired
    private AccountManagementService accountManagementService;

    @PutMapping("/{customerId}/wishlist/{gameId}")
    public void addGameToWishlist(@PathVariable int customerId, @PathVariable int gameId) {
        accountManagementService.addGameToWishlist(customerId, gameId);
    }

    @DeleteMapping("/{customerId}/wishlist/{gameId}")
    public void removeGameFromWishlist(@PathVariable int customerId, @PathVariable int gameId) {
        accountManagementService.removeGameFromWishlist(customerId, gameId);
    }

    @GetMapping("/{customerId}/wishlist")
    public List<GameDTO> viewWishlist(@PathVariable int customerId) {
        Set<Game> wishlist = accountManagementService.viewWishlist(customerId);
        return wishlist.stream().map(GameDTO::new).collect(Collectors.toList());
    }


}

