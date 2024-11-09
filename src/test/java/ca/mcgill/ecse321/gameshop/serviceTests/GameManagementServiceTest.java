package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.model.Category;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameManagementServiceTest {
    @InjectMocks
    GameManagementService gameManagementService;
    @Mock
    private CategoryRepository categoryRepo;
    @Mock
    private GameRepository gameRepo;
    @Mock
    private CustomerRepository customerRepo;

    private Category referenceCategory = new Category("test category");
    private Game referenceGame1 = new Game("name1","test desc1","test cover1", 23.33f, true, 40);
    private Customer referenceCustomer = new Customer("John Doe","password","john@doe.com","315-444-2222");
    private Category mostRecentSavedCategory;
    private Category mostRecentDeletedCategory;

    @AfterEach
    public void tearDown() {
        mostRecentSavedCategory = null;
        mostRecentDeletedCategory = null;
    }
    @BeforeEach
    public void setUp() {
        when(categoryRepo.findByName(referenceCategory.getName())).thenReturn(Optional.of(referenceCategory));
        when(categoryRepo.save(any(Category.class))).then((Answer<Category>) invocation -> {
            mostRecentSavedCategory = invocation.getArgument(0);
            return invocation.getArgument(0);
        });
        doAnswer(invocation -> {
            mostRecentDeletedCategory = invocation.getArgument(0);
            return null;
        }).when(categoryRepo).delete(any(Category.class));

        when(gameRepo.findById(referenceGame1.getId())).thenReturn(Optional.of(referenceGame1));

        when(customerRepo.findById(referenceCustomer.getId())).thenReturn(Optional.of(referenceCustomer));

        referenceCategory.addInCategory(referenceGame1);
    }

    @Test
    public void testCreateCategory(){
        String testName = "testing name";
        gameManagementService.createCategory(testName);

        assertNotNull(mostRecentSavedCategory);
        assertEquals(testName, mostRecentSavedCategory.getName());
    }

    @Test
    public void testDeleteValidCategory(){
        gameManagementService.deleteCategory(referenceCategory.getName());

        assertNotNull(mostRecentDeletedCategory);
        assertEquals(referenceCategory, mostRecentDeletedCategory);
        assertTrue(referenceGame1.getCopyCategories().isEmpty());
    }

    @Test
    public void testDeleteInvalidCategory(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.deleteCategory("non existent category"));
        assertNull(mostRecentDeletedCategory);
    }

    @Test
    public void testGetGamesInValidCategory(){
        Set<Game> gamesInCategory = gameManagementService.getGamesInCategory(referenceCategory.getName());

        assertNotNull(gamesInCategory);
        assertEquals(1, gamesInCategory.size());
        assertTrue(gamesInCategory.contains(referenceGame1));
    }
    @Test
    public void testGetGamesInInvalidCategory(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.getGamesInCategory("non existent category"));
    }

    @Test
    public void testAddValidGameToValidCart(){
        gameManagementService.addGameToCart(referenceCustomer.getId(), referenceGame1.getId());
        Set<Game> cart = referenceCustomer.getCopyCart();

        assertEquals(1, cart.size());
        assertTrue(referenceCustomer.containsGameInCart(referenceGame1));
        assertTrue(referenceGame1.containsInCartOf(referenceCustomer));
    }

    @Test
    public void testAddInvalidGameToValidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.addGameToCart(referenceCustomer.getId(), -1));
        assertTrue(referenceCustomer.getCopyCart().isEmpty());
    }

    @Test
    public void testAddValidGameToInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.addGameToCart(-1, referenceGame1.getId()));
    }

    @Test
    public void testAddInvalidGameToInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.addGameToCart(-1, -1));
    }

    @Test
    public void testViewGamesInInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.viewGamesInCart(-1));
    }

    @Test
    public void testViewGamesInValidCart(){
        referenceCustomer.addGameToCart(referenceGame1);

        Set<Game> gamesInCart = gameManagementService.viewGamesInCart(referenceCustomer.getId());

        assertNotNull(gamesInCart);
        assertEquals(1, gamesInCart.size());
        assertTrue(gamesInCart.contains(referenceGame1));
    }

    @Test
    public void testRemoveValidGameInValidCart(){
        referenceCustomer.addGameToCart(referenceGame1);
        gameManagementService.removeGameFromCart(referenceCustomer.getId(), referenceGame1.getId());

        assertTrue(referenceCustomer.getCopyCart().isEmpty());
        assertTrue(referenceGame1.getCopyInCartOf().isEmpty());
    }
    @Test
    public void testRemoveValidGameNotInValidCart(){
        referenceCustomer.addGameToCart(referenceGame1);

        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.removeGameFromCart(referenceCustomer.getId(), referenceGame1.getId()+1));
        assertEquals(1, referenceCustomer.getCopyCart().size());
    }

    @Test
    public void testRemoveValidGameInInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.removeGameFromCart(-1, referenceGame1.getId()+1));
    }

    @Test
    public void testViewInventory() {
        MockitoAnnotations.openMocks(this);
        // Arrange
        List<Game> games = List.of(new Game("Game1", "Description1", "Cover1", 50.0f, true, 10));
        when(gameRepo.findAll()).thenReturn(games);

        // Act
        Set<Game> inventory = gameManagementService.viewInventory();

        // Assert
        assertNotNull(inventory);
        assertEquals(1, inventory.size());
    }

    @Test
    public void testUpdateInventoryAfterPurchase() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        when(gameRepo.findById(referenceGame1.getId())).thenReturn(Optional.of(referenceGame1));

        // Act
        gameManagementService.updateInventoryAfterPurchase(referenceGame1.getId(), 5);

        // Assert
        assertEquals(35, referenceGame1.getStock(), "Stock should decrease by the purchased amount");
        verify(gameRepo, times(1)).save(referenceGame1);
    }

    @Test
    public void testUpdateInventoryAfterPurchaseWithInsufficientStock() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        when(gameRepo.findById(referenceGame1.getId())).thenReturn(Optional.of(referenceGame1));

        // Act & Assert
        assertThrows(IllegalStateException.class, 
                    () -> gameManagementService.updateInventoryAfterPurchase(referenceGame1.getId(), 50),
                    "Should throw exception when stock is insufficient");
        verify(gameRepo, times(0)).save(referenceGame1);
    } 
    
    @Test
    public void testUpdateStockManually() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        when(gameRepo.findById(referenceGame1.getId())).thenReturn(Optional.of(referenceGame1));

        // Act
        gameManagementService.updateStock(referenceGame1.getId(), 10);

        // Assert
        assertEquals(50, referenceGame1.getStock(), "Stock should increase by the given amount");
        verify(gameRepo, times(1)).save(referenceGame1);
    }

    @Test
    public void testUpdateStockManuallyWithInvalidGame() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        when(gameRepo.findById(-1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
                    () -> gameManagementService.updateStock(-1, 10),
                    "Should throw exception when the game ID is invalid");
        verify(gameRepo, times(0)).save(any(Game.class));
    }


}
