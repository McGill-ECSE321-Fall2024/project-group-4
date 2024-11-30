package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.CartItemRepository;
import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.model.CartItem;
import ca.mcgill.ecse321.gameshop.model.Category;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestGameManagementService {
    @InjectMocks
    GameManagementService gameManagementService;
    @Mock
    private CategoryRepository categoryRepo;
    @Mock
    private GameRepository gameRepo;
    @Mock
    private CustomerRepository customerRepo;
    @Mock
    private CartItemRepository cartItemRepo;

    private Category referenceCategory = new Category("test category");
    private Game referenceGame1 = new Game("name1","test desc1","test cover1", 23.33f, true, 40);
    @Spy
    private Game referenceGame2 = new Game("name2","test desc2","test cover2", 54.50f, true, 320);
    private Customer referenceCustomer = new Customer("John Doe","password","john@doe.com","315-444-2222");
    private Set<CartItem> savedCartItems;
    private CartItem referenceCartItem = new CartItem(1, referenceCustomer, referenceGame1);

    @AfterEach
    public void tearDown() {
        savedCartItems = null;
    }
    @BeforeEach
    public void setUp() {
        when(gameRepo.findById(referenceGame1.getId())).thenReturn(Optional.of(referenceGame1));
        //since persistence layer is not active, both reference games will have the same ID (both 0)
        //as a result, we are mocking as if the referenceGame2 has id of 1 and referenceGame1 has id of 0
        when(referenceGame2.getId()).thenReturn(referenceGame1.getId()+1);
        when(gameRepo.findById(referenceGame2.getId())).thenReturn(Optional.of(referenceGame2));

        when(gameRepo.findAll()).thenReturn(List.of(referenceGame1, referenceGame2));

        when(customerRepo.findById(referenceCustomer.getId())).thenReturn(Optional.of(referenceCustomer));
        when(customerRepo.existsById(referenceCustomer.getId())).thenReturn(true);

        savedCartItems = new HashSet<>();
        savedCartItems.add(referenceCartItem);

        when(cartItemRepo.findByCartItemId_Customer_IdAndCartItemId_Game_Id(referenceCustomer.getId(), referenceGame1.getId()))
                .thenReturn(Optional.of(referenceCartItem));

        referenceCategory.addInCategory(referenceGame1);
        when(categoryRepo.findByName(referenceCategory.getName())).thenReturn(Optional.of(referenceCategory));
    }

    @Test
    public void testCreateCategory(){
        String testName = "testing name";
        gameManagementService.createCategory(testName);
        verify(categoryRepo, times(1))
                .save(argThat((category)->category.getName().equals(testName)));
    }

    @Test
    public void testDeleteValidCategory(){
        gameManagementService.deleteCategory(referenceCategory.getName());

        verify(categoryRepo, times(1)).delete(referenceCategory);
        assertTrue(referenceGame1.getCopyCategories().isEmpty());
        verify(gameRepo, times(1)).save(referenceGame1);
    }

    @Test
    public void testDeleteInvalidCategory(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.deleteCategory("non existent category"));
        verify(categoryRepo, times(0)).delete(any(Category.class));
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
    public void testAddGameToCategory(){
        assertFalse(referenceCategory.containsInCategory(referenceGame2));
        assertFalse(referenceGame2.containsCategory(referenceCategory));

        gameManagementService.addGameToCategory(referenceCategory.getName(), referenceGame2.getId());

        assertTrue(referenceCategory.containsInCategory(referenceGame2));
        assertTrue(referenceGame2.containsCategory(referenceCategory));
        verify(gameRepo, times(1)).save(referenceGame2);
    }

    @Test
    public void testAddGameToCategoryAlreadyInCategory(){
        assertTrue(referenceCategory.containsInCategory(referenceGame1));
        assertTrue(referenceGame1.containsCategory(referenceCategory));

        assertThrows(IllegalArgumentException.class, () -> gameManagementService.addGameToCategory(referenceCategory.getName(), referenceGame1.getId()));

        verify(gameRepo, times(0)).save(any());
        verify(categoryRepo, times(0)).save(any());
    }

    @Test
    public void testRemoveGameFromCategory(){
        assertTrue(referenceCategory.containsInCategory(referenceGame1));
        assertTrue(referenceGame1.containsCategory(referenceCategory));

        gameManagementService.removeGameFromCategory(referenceCategory.getName(), referenceGame1.getId());

        assertFalse(referenceCategory.containsInCategory(referenceGame1));
        assertFalse(referenceGame1.containsCategory(referenceCategory));
        verify(categoryRepo, times(1)).save(referenceCategory);
        verify(gameRepo, times(1)).save(referenceGame1);
    }

    @Test
    public void testRemoveGameFromCategoryNotInCategory(){
        assertFalse(referenceCategory.containsInCategory(referenceGame2));
        assertFalse(referenceGame2.containsCategory(referenceCategory));

        assertThrows(IllegalArgumentException.class, () -> gameManagementService.removeGameFromCategory(referenceCategory.getName(), referenceGame2.getId()));

        verify(categoryRepo, times(0)).save(any());
        verify(gameRepo, times(0)).save(any());
    }

    @Test
    public void testAddValidNotAlreadyPresentGameToValidCart(){
        when(cartItemRepo.save(any(CartItem.class))).then((Answer<CartItem>) invocation -> {
            savedCartItems.add(invocation.getArgument(0));
            return invocation.getArgument(0);
        });

        CartItem expectedCartItem1 = new CartItem(1, referenceCustomer, referenceGame1);
        CartItem expectedCartItem2 = new CartItem(1, referenceCustomer, referenceGame2);

        gameManagementService.addGameToCart(referenceCustomer.getId(), referenceGame2.getId());

        assertEquals(2, savedCartItems.size());
        assertTrue(savedCartItems.contains(expectedCartItem1));
        assertTrue(savedCartItems.contains(expectedCartItem2));
        verify(cartItemRepo, times(1)).save(expectedCartItem2);
    }

    @Test
    public void testAddValidAlreadyPresentGameToValidCart(){
        CartItem expectedCartItem1 = new CartItem(2, referenceCustomer, referenceGame1);

        gameManagementService.addGameToCart(referenceCustomer.getId(), referenceGame1.getId());

        assertEquals(1, savedCartItems.size());
        CartItem savedCartItem = savedCartItems.stream().findFirst().get();
        assertEquals(expectedCartItem1, savedCartItem);
        assertEquals(expectedCartItem1.getQuantity(), savedCartItem.getQuantity());
    }

    @Test
    public void testAddInvalidGameToValidCart(){
        Exception e = assertThrows(EntityNotFoundException.class, ()-> gameManagementService.addGameToCart(referenceCustomer.getId(), -1));
        assertEquals("Game to add to cart not found", e.getMessage());
        assertEquals(1, savedCartItems.size());
        verify(cartItemRepo, times(0)).save(any(CartItem.class));
    }

    @Test
    public void testAddValidGameToInvalidCart(){
        Exception e = assertThrows(EntityNotFoundException.class, ()-> gameManagementService.addGameToCart(-1, referenceGame1.getId()));
        assertEquals("Customer to add game to cart of not found", e.getMessage());
        verify(cartItemRepo, times(0)).save(any(CartItem.class));
    }

    @Test
    public void testAddInvalidGameToInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.addGameToCart(-1, -1));
        verify(cartItemRepo, times(0)).save(any(CartItem.class));
    }

    @Test
    public void testViewGamesInInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.viewGamesInCart(-1));
    }

    @Test
    public void testViewGamesInValidCart(){
        when(cartItemRepo.findByCartItemId_Customer_Id(referenceCustomer.getId()))
                .thenReturn(savedCartItems.stream().filter(
                                (cartItem -> cartItem.getCustomer().getId() == referenceCustomer.getId()))
                        .collect(Collectors.toSet()));

        Set<CartItem> gamesInCart = gameManagementService.viewGamesInCart(referenceCustomer.getId());

        assertNotNull(gamesInCart);
        assertEquals(1, gamesInCart.size());
        assertTrue(gamesInCart.stream().anyMatch((cartItem -> cartItem.getGame().getId() == referenceGame1.getId())));
    }

    @Test
    public void testRemoveValidGameInValidCart(){
        when(gameRepo.existsById(referenceGame1.getId())).thenReturn(true);
        when(gameRepo.existsById(referenceGame2.getId())).thenReturn(true);

        gameManagementService.removeGameFromCart(referenceCustomer.getId(), referenceGame1.getId());
        verify(cartItemRepo, times(1)).delete(eq(referenceCartItem));
    }
    @Test
    public void testRemoveValidGameNotInValidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.removeGameFromCart(referenceCustomer.getId(), referenceGame2.getId()));
        assertEquals(1, savedCartItems.size());
    }

    @Test
    public void testRemoveValidGameInInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.removeGameFromCart(-1, referenceGame2.getId()));
    }

    @Test
    public void testViewInventory() {
        // Act
        Set<Game> inventory = gameManagementService.viewInventory();

        // Assert
        assertNotNull(inventory);
        assertEquals(2, inventory.size());
    }

    @Test
    public void testUpdateStock() {
        int newStock = 5;
        // Act
        gameManagementService.updateStock(referenceGame1.getId(), newStock);

        // Assert
        assertEquals(newStock, referenceGame1.getStock(), "Stock should equal the new stock number");
        verify(gameRepo, times(1)).save(referenceGame1);
    }

    @Test
    public void testUpdateStockWithNegativeNumber() {
        // Act & Assert
        assertThrows(IllegalStateException.class, 
                    () -> gameManagementService.updateStock(referenceGame1.getId(), -1),
                    "Should throw exception when stock is negative");
        verify(gameRepo, times(0)).save(referenceGame1);
    }

    @Test
    public void testUpdateStockWithInvalidGame() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
                    () -> gameManagementService.updateStock(-1, 10),
                    "Should throw exception when the game ID is invalid");
        verify(gameRepo, times(0)).save(any(Game.class));
    }

    @Test
    public void testUpdateGameActive() {
        //Act
        gameManagementService.updateActivity(referenceGame1.getId(), false);

        //Assert
        assertFalse(referenceGame1.isActive());
        verify(gameRepo, times(1)).save(referenceGame1);
    }
}
