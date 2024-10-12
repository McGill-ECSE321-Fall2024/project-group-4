package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.persistence.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Author: Clara Mickail
 */
@SpringBootTest
public class TestCustomer {

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCustomer() {
        // Create object
        String username = "testUser";
        String password = "testPass";
        String email = "testEmail@gmail.com";
        String phoneNumber = "5145555555";
        Customer customer = new Customer(username, password, email, phoneNumber);

        // Save
        customer = customerRepository.save(customer);
        int id = customer.getId();

        // Read
        Customer customerFromDb = customerRepository.findById(id).orElse(null);

        // Assert
        assertNotNull(customerFromDb);
        assertEquals(username, customerFromDb.getUsername());
        assertEquals(password, customerFromDb.getPassword());
        assertEquals(email, customerFromDb.getEmail());
        assertEquals(phoneNumber, customerFromDb.getPhoneNumber());
    }

    @Test
    public void testFindByEmail() {
        // Create object
        String username = "testUser2";
        String password = "testPass2";
        String email = "testEmail2@gmail.com";
        String phoneNumber = "514-444-4444";
        Customer customer = new Customer(username, password, email, phoneNumber);

        // Save 
        customerRepository.save(customer);

        // Read
        Customer customerFromDb = customerRepository.findByEmail(email);

        // Assert
        assertNotNull(customerFromDb);
        assertEquals(email, customerFromDb.getEmail());
    }

    @Test
    public void testFindByPhoneNumber() {
        // Create object
        String username = "testUser3";
        String password = "testPass3";
        String email = "testEmail3@gmail.com";
        String phoneNumber = "5143333333";
        Customer customer = new Customer(username, password, email, phoneNumber);

        // Sav
        customerRepository.save(customer);

        // Read
        List<Customer> customersFromDb = customerRepository.findByPhoneNumber(phoneNumber);

        // Assert
        assertFalse(customersFromDb.isEmpty());
        assertEquals(phoneNumber, customersFromDb.get(0).getPhoneNumber());
    }
}
