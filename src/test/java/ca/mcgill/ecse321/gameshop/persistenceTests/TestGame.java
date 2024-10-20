package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.model.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Author : Tarek Namani
 */
@SpringBootTest
public class TestGame {
    @Autowired
    private GameRepository gameRepostitory;

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
        game.setName("testame");
        game.setDescription("testDescription");
        game.setCoverPicture("testCover");
        game.setPrice(50.00F);
        game.setStock(100);
        game.setActive(true);
    }

    @AfterEach
    public void tearDown() {
        gameRepostitory.deleteAll();
    }

    @Test
    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading a Game object from the database
     */
    public void testSaveAndLoadGame() {
        //save
        gameRepostitory.save(game);

        //load
        Optional<Game> loadedGameOpt = gameRepostitory.findById(game.getId());

        //compare
        assertTrue(loadedGameOpt.isPresent());
        Game loadedGame = loadedGameOpt.get();
        assertEquals(game.getId(),loadedGame.getId());
        assertEquals(game.getName(),loadedGame.getName());
        assertEquals(game.getDescription(),loadedGame.getDescription());
        assertEquals(game.getPrice(),loadedGame.getPrice());
        assertEquals(game.getCoverPicture(),loadedGame.getCoverPicture());
        assertEquals(game.isActive(),loadedGame.isActive());
        assertEquals(game.getStock(),loadedGame.getStock());


    }
}
