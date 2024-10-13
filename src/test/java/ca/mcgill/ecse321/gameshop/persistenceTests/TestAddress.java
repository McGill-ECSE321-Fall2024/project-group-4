package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.AddressRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.model.Address;
import ca.mcgill.ecse321.gameshop.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Clara Mickail
 */
@SpringBootTest
public class TestAddress {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void clearDatabase() {
        addressRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadAddress() throws NoSuchFieldException, IllegalAccessException {
        // Create a Customer
        String customerUsername = "testUser";
        String customerPassword = "testPass";
        Customer customer = new Customer(customerUsername, customerPassword, "testUser@gmail.com", "5144444444");
        //save customer to database
        customer = customerRepository.save(customer);

        // Create Address
        String street = "123 Peel St";
        String city = "Montreal";
        String province = "Quebec";
        String country = "Canada";
        String postalCode = "H4Y 2M8";
        Address address = new Address(street, city, province, country, postalCode, customer);
        // Save Address to database
        address = addressRepository.save(address);
        int id = address.getId();

        // Read address
        Address addressFromDb = addressRepository.findById(id).orElse(null);

        // Assert address
        assertNotNull(addressFromDb);
        assertEquals(street, addressFromDb.getStreet());
        assertEquals(city, addressFromDb.getCity());
        assertEquals(province, addressFromDb.getProvince());
        assertEquals(country, addressFromDb.getCountry());
        assertEquals(postalCode, addressFromDb.getPostalCode());

        // read customer
        Field customerField = Address.class.getDeclaredField("customer");
        customerField.setAccessible(true);
        Customer customerFromDb = (Customer) customerField.get(addressFromDb);

        // Assert customer
        assertEquals(customer.getId(), customerFromDb.getId());
    }
}
