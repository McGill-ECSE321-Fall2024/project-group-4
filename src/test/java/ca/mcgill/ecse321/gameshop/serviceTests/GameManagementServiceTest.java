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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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
    private Game referenceGame2 = new Game("name2","test desc2","test cover2", 54.50f, true, 320);
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
        when(gameRepo.findById(referenceGame2.getId())).thenReturn(Optional.of(referenceGame2));

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

        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.removeGameFromCart(referenceCustomer.getId(), referenceGame2.getId()));
        assertEquals(1, referenceCustomer.getCopyCart().size());
    }

    @Test
    public void testRemoveValidGameInInvalidCart(){
        assertThrows(EntityNotFoundException.class, ()-> gameManagementService.removeGameFromCart(-1, referenceGame2.getId()));
    }








}
