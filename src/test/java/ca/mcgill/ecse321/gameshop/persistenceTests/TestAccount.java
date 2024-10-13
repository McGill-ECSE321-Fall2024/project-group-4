package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.AccountRepository;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Clara Mickail
 * 
 * Since Account is abstract, I used Customer for testing
 */
@SpringBootTest
public class TestAccount {

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void clearDatabase() {
        accountRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadAccount() {
        // test object
        String username = "testUser";
        String password = "testPass";
        Customer customer = new Customer(username, password, "testUser@example.com", "1234567890");

        // Save
        customer = accountRepository.save(customer);
        int id = customer.getId();

        // Read 
        Customer readFromDb = (Customer) accountRepository.findById(id).orElse(null);
        assertNotNull(readFromDb);

        //assert
        assertEquals(username, readFromDb.getUsername());
        assertEquals(password, readFromDb.getPassword());
    }

    @Test
    public void testFindByUsername() {
        // test object
        String username = "testUser2";
        String password = "testPass2";
        Customer customer = new Customer(username, password, "testUser2@example.com", "5144444444");

        // Save
        accountRepository.save(customer);

        //read
        Account accountFromDb = accountRepository.findByUsername(username);

        // Assert
        assertNotNull(accountFromDb);
        assertEquals(username, accountFromDb.getUsername());
        assertEquals(password, accountFromDb.getPassword());
    }
}
