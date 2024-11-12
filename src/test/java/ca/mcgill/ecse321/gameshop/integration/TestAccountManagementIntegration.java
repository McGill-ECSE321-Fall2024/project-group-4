package ca.mcgill.ecse321.gameshop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.gameshop.DAO.AccountRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;
import ca.mcgill.ecse321.gameshop.DAO.ManagerRepository;
import ca.mcgill.ecse321.gameshop.dto.AccountDTO;
import ca.mcgill.ecse321.gameshop.dto.CustomerDTO;


import ca.mcgill.ecse321.gameshop.model.Account;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.model.Manager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAccountManagementIntegration {

    @Autowired
    private TestRestTemplate account;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private String USERNAME = "testUsernameValid";
    private String PASSWORD = "testPasswordValid";
    private String EMAIL_STRING = "emailvalid@email.com";
    private String EMAIL_STRING_INVALID = "invalid email";
    private String PHONENUMBER_STRING = "0123456789";
    private int CUSTOMER_ID;
    private int INVALID_ACCOUNTID = -1;
    private String INVALID_USERNAME = "testUsername Not Valid";
    private String INVALID_PASSWORD = "testPassword Not Valid";
    private int INVALID_ACCOUNTROLEID = -1;
    private String OLD_USERNAME = "testUsername_old";
    private String OLD_PASSWORD = "testPassword_old";

    private AccountManagementService accountManagementService;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    /**
     * Create a valid customer account
     * 
     * @author Ana Gordon
     */
    @Test
    @Order(1)
    public void createCustomerAccount()  {
        //Arrange
        CustomerDTO customer = new CustomerDTO(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING, null, null, null, null, null);

        //Act
        ResponseEntity<CustomerDTO> response = account.postForEntity("/customers/"  + EMAIL_STRING, customer, CustomerDTO.class);
        
        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); //check if HttpStatus.CREATED is correct

        CustomerDTO responseCustomer = response.getBody();
        assertNotNull(responseCustomer);
        assertEquals(USERNAME, responseCustomer.getUsername());
        assertEquals(PASSWORD, responseCustomer.getPassword());
        // assertEquals(EMAIL_STRING, responseCustomer.getEmail());
        // assertNotNull(responseCustomer.getId());
        // assertTrue(responseCustomer.getId() > 0, "Response should have a positive ID.");
        assertNotNull(customerRepository.findByEmail(EMAIL_STRING));
        // this.CUSTOMER_ID = responseCustomer.getId();  
    }

    
}