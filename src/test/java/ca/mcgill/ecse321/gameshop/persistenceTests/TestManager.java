package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.ManagerRepository;
import ca.mcgill.ecse321.gameshop.persistence.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * Author : Tarek Namani
 */
@SpringBootTest
public class TestManager {

    @Autowired
    ManagerRepository managerRepository;

    Manager manager = new Manager();

    @AfterEach
    public void tearDown() {
        managerRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        manager = new Manager();
        manager.setPassword("safePassword");
        manager.setUsername("Bob");
    }
    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading a Manager object from the database
     */
    @Test
    public void testSaveAndLoadManager() {
        //save
        managerRepository.save(manager);

        //load
        Manager loadedManager = managerRepository.findById(manager.getId());

        //compare
        assertEquals(manager.getId(), loadedManager.getId());
        assertEquals(manager.getUsername(), loadedManager.getUsername());
        assertEquals(manager.getPassword(), loadedManager.getPassword());

    }
}
