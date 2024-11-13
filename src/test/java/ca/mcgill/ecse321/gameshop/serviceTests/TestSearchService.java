package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test search games service
 *
 * @author Camille Pouliot
 */
@SpringBootTest
public class TestSearchService {

    @InjectMocks
    private GameManagementService searchService;

    @Mock
    private GameRepository gameRepository;

    Game game;
    Game game2;
    Game inactiveGame;

    @BeforeEach
    public void setUp() {
        game = new Game("Game Name", "Game Description", "Game Picture", 10, true, 1);
        game2 = new Game("Other Game Name", "Other Game Description", "Other Game Picture", 15, true, 1);
        inactiveGame = new Game("Inactive Game Name", "Inactive Game Description", "Inactive Game Picture", 10, false, 1);

        Set<Game> games = new HashSet<>();
        games.add(game);
        games.add(game2);
        games.add(inactiveGame);

        when(gameRepository.findAll()).thenReturn(games);
    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
    }

    /**
     * Test searching games with a one word string
     *
     * @author Camille Pouliot
     */
    @Test
    public void testOneWordSearch() {
        //Act
        Set<Game> searchedGames = searchService.searchGames("Other");

        //Assert
        assertNotNull(searchedGames);
        assertEquals(1, searchedGames.size());
        assertTrue(searchedGames.contains(game2));
        verify(gameRepository, times(1)).findAll();
    }

    /**
     * Test search games with a two word string
     *
     * @author Camille Pouliot
     */
    @Test
    public void testTwoWordSearch() {
        //Act
        Set<Game> searchedGames = searchService.searchGames("Game Name");

        //Assert
        assertNotNull(searchedGames);
        assertEquals(2, searchedGames.size());
        assertTrue(searchedGames.contains(game2));
        assertTrue(searchedGames.contains(game));
        verify(gameRepository, times(1)).findAll();
    }

    /**
     * Test searching an empty string
     *
     * @author Camille Pouliot
     */
    @Test
    public void testEmptySearch() {
        //Act
        Set<Game> searchedGames = searchService.searchGames("");

        //Assert
        assertNotNull(searchedGames);
        assertEquals(2, searchedGames.size());
        assertTrue(searchedGames.contains(game2));
        assertTrue(searchedGames.contains(game));
        verify(gameRepository, times(1)).findAll();
    }

    /**
     * Test searching a string with no match
     *
     * @author Camille Pouliot
     */
    @Test
    public void testSearchNoMatch() {
        //Act
        Set<Game> searchedGames = searchService.searchGames("No Game");

        //Assert
        assertNotNull(searchedGames);
        assertEquals(0, searchedGames.size());
        verify(gameRepository, times(1)).findAll();
    }

}