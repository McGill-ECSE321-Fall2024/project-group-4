package ca.mcgill.ecse321.gameshop.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

@SpringBootTest
public class AccountManagementServiceTest {

    @InjectMocks
    private AccountManagementService accountManagementService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GameRepository gameRepository;

    private Customer referenceCustomer;
    private Game referenceGame;

    @BeforeEach
    public void setUp() {
        referenceCustomer = new Customer("testUser", "password", "test@gmail.com", "1234567890");
        referenceGame = new Game("Test Game", "Test Description", "Test Cover", 50.0f, true, 10);

        when(customerRepository.findById(referenceCustomer.getId())).thenReturn(Optional.of(referenceCustomer));
        when(gameRepository.findById(referenceGame.getId())).thenReturn(Optional.of(referenceGame));
    }

    @Test
    public void testAddGameToWishlist() {
        accountManagementService.addGameToWishlist(referenceCustomer.getId(), referenceGame.getId());

        assertTrue(referenceCustomer.containsGameInWishlist(referenceGame));
        verify(customerRepository, times(1)).save(referenceCustomer);
    }

    @Test
    public void testRemoveGameFromWishlist() {
        referenceCustomer.addGameToWishlist(referenceGame);

        accountManagementService.removeGameFromWishlist(referenceCustomer.getId(), referenceGame.getId());

        assertFalse(referenceCustomer.containsGameInWishlist(referenceGame));
        verify(customerRepository, times(1)).save(referenceCustomer);
    }

    @Test
    public void testViewWishlist() {
        referenceCustomer.addGameToWishlist(referenceGame);

        Set<Game> wishlist = accountManagementService.viewWishlist(referenceCustomer.getId());

        assertEquals(1, wishlist.size());
        assertTrue(wishlist.contains(referenceGame));
    }
}

