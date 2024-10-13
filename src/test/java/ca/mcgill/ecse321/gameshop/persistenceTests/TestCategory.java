package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.CategoryRepository;
import ca.mcgill.ecse321.gameshop.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Clara Mickail
 */
@SpringBootTest
public class TestCategory {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void clearDatabase() {
        categoryRepository.deleteAll();
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
        Category categoryFromDb = categoryRepository.findByName(categoryName);

        // Assert
        assertNotNull(categoryFromDb);
        assertEquals(categoryName, categoryFromDb.getName());
    }
}
