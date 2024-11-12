package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.CartItemRepository;
import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.model.CartItem;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional
    public void addGameToCart(int customerId, int gameId){
        Game gameToAdd = gameRepository.findById(gameId).orElseThrow(()-> new EntityNotFoundException("Game to add to cart not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new EntityNotFoundException("Customer to add game to cart of not found"));

        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartItemId_Customer_IdAndCartItemId_Game_Id(customerId, gameId);

        if(existingCartItemOpt.isPresent()){
            CartItem existingCartItem = existingCartItemOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity()+1);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem(1, customer, gameToAdd);
            cartItemRepository.save(cartItem);
        }

    }

    public Set<Game> viewGamesInCart(int customerId){
        if(!customerRepository.existsById(customerId)){
            throw new EntityNotFoundException("Customer to add game to cart of not found");
        }
        Set<CartItem> itemsInCart = cartItemRepository.findByCartItemId_Customer_Id(customerId);
        return itemsInCart.stream().map((CartItem::getGame)).collect(Collectors.toSet());
    }
    @Transactional
    public void removeGameFromCart(int customerId, int gameId){
        if(!gameRepository.existsById(gameId) || !customerRepository.existsById(customerId)){
            throw new EntityNotFoundException("Game to remove from cart or customer not found");
        }

        CartItem existingCartItem = cartItemRepository.findByCartItemId_Customer_IdAndCartItemId_Game_Id(customerId, gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game to remove was not in customer's cart"));

        cartItemRepository.delete(existingCartItem);
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

        for(Game game : category.getCopyInCategory()){
            if(!game.removeCategory(category)){
                throw new IllegalStateException("Could not remove games from category to delete");
            }
            gameRepository.save(game);
        }
        categoryRepo.delete(category);
    }

    public Set<Game> getGamesInCategory(String name){
        var category = categoryRepo.findByName(name).orElseThrow(()-> new EntityNotFoundException("Category to get games in was not found"));
        return category.getCopyInCategory();

    }

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
                                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
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
    public void updateStock(int gameId, int newStock) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        if (newStock < 0) {
            throw new IllegalStateException("Cannot set stock to negative number");
        }
        game.setStock(newStock);
        gameRepository.save(game);
    }


    


}
    