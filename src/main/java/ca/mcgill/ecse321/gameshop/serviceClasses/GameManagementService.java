package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.model.Category;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Game;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GameManagementService {
    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;
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

    public Set<Game> viewInventory() {
        // Convert Iterable<Game> to HashSet<Game>
        Iterable<Game> games = gameRepository.findAll();
        Set<Game> gameSet = new HashSet<>();
        games.forEach(gameSet::add);
        return gameSet;
    }

    @Transactional
    public Game addNewGame(String name, String description, String cover, float price, boolean isActive, int stock, List<String> categoryNames) {
        // Create a new game
        Game newGame = new Game(name, description, cover, price, isActive, stock);
        gameRepository.save(newGame);

        // Find each category and associate it with the new game
        for (String categoryName : categoryNames) {
            var category = categoryRepo.findByName(categoryName)
                                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));
            category.addInCategory(newGame);
            categoryRepo.save(category);
        }

        return newGame;
    }


    @Transactional
    public void removeGame(int gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        game.setAvailable(false);
        gameRepository.save(game);
    }

    @Transactional
    public void updateInventoryAfterPurchase(int gameId, int quantityPurchased) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        if (game.getStock() < quantityPurchased) {
            throw new IllegalStateException("Insufficient stock for the game");
        }
        game.updateStock(-quantityPurchased);
        gameRepository.save(game);
    }

    /**
     * Allows staff to manually update inventory
     * @param gameId
     * @param stockChange
     */
    @Transactional
    public void updateStock(int gameId, int stockChange) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        game.updateStock(stockChange);
        gameRepository.save(game);
    }

    


}
    