package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.model.CreditCard;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Address;
import ca.mcgill.ecse321.gameshop.DAO.CreditCardRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.AddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestCreditCard {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @AfterEach
    public void clearDatabase() {
        creditCardRepository.deleteAll();
        addressRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testPersistAndLoadCreditCard() {
        // Create object
        Customer customer = new Customer();
        customer.setUsername("JohnDoey");
        customer.setEmail("johndoey@gmail.com");
        customer.setPhoneNumber("5144444444");
        customer.setPassword("password123");

        // save 
        customer = customerRepository.save(customer); 

       // assert
        assertNotNull(customer.getId());


        //Create Address object
        Address address = new Address("123 Peel St", "Montreal", "Quebec", "Canada", "H3Z2Y7", customer);
        
        //save 
        address = addressRepository.save(address); 

        //assert
        assertNotNull(address.getId());

        // Step 3: Create CreditCard object
        int cardNumber = 1234511890;
        int cvv = 111;
        LocalDate expiryDate = LocalDate.of(2025, 12, 31);
        CreditCard creditCard = new CreditCard(cardNumber, cvv, expiryDate, customer, address);

        //save
        creditCard = creditCardRepository.save(creditCard);
        int cardId = creditCard.getId();

        //assert
        assertNotNull(creditCard.getId()); 

        // read
        CreditCard creditCardFromDb = creditCardRepository.findCreditCardById(cardId);

        //assert
        assertNotNull(creditCardFromDb);
        assertEquals(creditCardFromDb.getCardNumber(), cardNumber);
        assertEquals(creditCardFromDb.getCvv(), cvv);
        assertEquals(creditCardFromDb.getExpiryDate(), expiryDate);
        assertEquals(creditCardFromDb.getCustomer().getId(), customer.getId());
        assertEquals(creditCardFromDb.getBillingAddress().getId(), address.getId());
    }
}
