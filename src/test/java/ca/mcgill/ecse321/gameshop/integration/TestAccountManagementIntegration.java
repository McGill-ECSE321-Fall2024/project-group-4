package ca.mcgill.ecse321.gameshop.integration;

import ca.mcgill.ecse321.gameshop.DAO.AccountRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;
import ca.mcgill.ecse321.gameshop.DAO.ManagerRepository;
import ca.mcgill.ecse321.gameshop.dto.CustomerDTO;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Autowired
    private AccountManagementService accountManagementService;

    @AfterAll
    @BeforeEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    // Login tests //

    /**
     * 
     * Login test for a valid customer account
     * 
     * @author Ana Gordon
     */
    @Test
    @Order(0)
    public void testLoginValidAccount() {
        //create a customer account
        Customer customer = accountManagementService.createCustomer(EMAIL_STRING, PASSWORD, USERNAME, PHONENUMBER_STRING);
        customerRepository.save(customer);

        //login to this customer account
        ResponseEntity<CustomerDTO> response = account.postForEntity("/accounts/login/customers/" + EMAIL_STRING, PASSWORD ,CustomerDTO.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // assertEquals(USERNAME, response.getBody().getUsername());
        // assertEquals(PASSWORD, response.getBody().getPassword());
    }
    

    
}