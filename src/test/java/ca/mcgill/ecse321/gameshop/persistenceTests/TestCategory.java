package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.model.Category;
import ca.mcgill.ecse321.gameshop.model.Game;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Clara Mickail
 */
@SpringBootTest
public class TestCategory {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GameRepository gameRepository;

    @AfterEach
    public void clearDatabase() {
        categoryRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCategory() {
        // Create object
        String categoryName = "Action";
        Category category = new Category(categoryName);

        // Save
        category = categoryRepository.save(category);
        int id = category.getId();

        // Read
        Category categoryFromDb = categoryRepository.findById(id).orElse(null);

        // Assert
        assertNotNull(categoryFromDb);
        assertEquals(categoryName, categoryFromDb.getName());
    }

    @Test
    public void testFindCategoryByName() {
        // Create
        String categoryName = "Adventure";
        Category category = new Category(categoryName);

        //save
        categoryRepository.save(category);

        // read
        Category categoryFromDb = categoryRepository.findByName(categoryName).orElseThrow();

        // Assert
        assertNotNull(categoryFromDb);
        assertEquals(categoryName, categoryFromDb.getName());
    }

    @Test @Transactional//test should be transational to properly initialze many to many associations
    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading the games in a category from databases
     */
    public void testGetGamesInCategory() {
        // Create
        String categoryName = "Adventure";
        Category category = new Category(categoryName);

        Game game = new Game("testgame", "testDescription", "testCover", 50.00f, true, 100);

        category.addInCategory(game);

        //save
        gameRepository.save(game);
        categoryRepository.save(category);

        //load
        Category categoryFromDb = categoryRepository.findByName(categoryName).orElseThrow();

        // Assert
        assertDoesNotThrow(categoryFromDb::getCopyInCategory); //asserting inCategory isn't null
        List<Game> aventureGamesFromDb = new ArrayList<>(categoryFromDb.getCopyInCategory()); //make a list to access the game in category
        assertEquals(game.getName(), aventureGamesFromDb.get(0).getName());



    }
}
