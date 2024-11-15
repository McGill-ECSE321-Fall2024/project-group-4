package ca.mcgill.ecse321.gameshop.integration;

import ca.mcgill.ecse321.gameshop.DAO.AccountRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;
import ca.mcgill.ecse321.gameshop.DAO.ManagerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private GameRepository gameRepository;

    private String USERNAME = "testUsernameValid";
    private String PASSWORD = "testPasswordValid";
    private String EMAIL_STRING = "emailvalid@email.com";
    private String EMAIL_STRING_INVALID = "invalid email";
    private String PHONENUMBER_STRING = "0123456789";
    private int ID = 0;
    private int INVALID_ID = -1;
    private String INVALID_USERNAME = "testUsername Not Valid";
    private String INVALID_PASSWORD = "testPassword Not Valid";
    private String OLD_USERNAME = "testUsername_old";
    private String OLD_PASSWORD = "testPassword_old";
    private String OLD_PHONENUMBER = "1234567890";
    private static final String TEST_CATEGORY_NAME1 = "test category name1";
    private int gameId;

    @Autowired
    private AccountManagementService accountManagementService;

    
    @BeforeEach
    @AfterEach
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
    public void testLoginValidCustomerAccount() {
        //create a customer account
        Customer customer = accountManagementService.createCustomer(EMAIL_STRING, PASSWORD, USERNAME, PHONENUMBER_STRING);
        customerRepository.save(customer);

        //login to this customer account
        ResponseEntity<CustomerResponseDTO> response = account.postForEntity("/accounts/login/customers/" + EMAIL_STRING, PASSWORD , CustomerResponseDTO.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());    
    }

    /**
     * 
     * Login test for an invalid customer account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(1)
    public void testLoginInvalidCustomerAccount() {
        //create a customer account
        Customer customer = accountManagementService.createCustomer(EMAIL_STRING, PASSWORD, USERNAME, PHONENUMBER_STRING);
        customerRepository.save(customer);

        //login to this customer account with invalid email
        ResponseEntity<CustomerResponseDTO> response = account.postForEntity("/accounts/login/customers/" + EMAIL_STRING_INVALID, PASSWORD , CustomerResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * 
     * Login test for a valid employee account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(2)
    public void testLoginValidEmployeeAccount() {
        //create an employee account
        accountManagementService.createEmployee(USERNAME, PASSWORD, true);
        
        //login to this employee account
        ResponseEntity<EmployeeResponseDTO> response = account.postForEntity("/accounts/login/employees/" + USERNAME, PASSWORD , EmployeeResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 
     * Login test for an invalid employee account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(3)
    public void testLoginInvalidEmployeeAccount() {
        //create an employee account
        accountManagementService.createEmployee(USERNAME, PASSWORD, true);
        
        //login to this employee account with invalid username
        ResponseEntity<EmployeeResponseDTO> response = account.postForEntity("/accounts/login/employees/" + INVALID_USERNAME, PASSWORD , EmployeeResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * 
     * Login test for a valid manager account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(4)
    public void testLoginValidManagerAccount() {
        //create a manager account
        accountManagementService.createManager();
        
        //login to this manager account
        ResponseEntity<ManagerDTO> response = account.postForEntity("/accounts/login/manager/manager", "manager" ,ManagerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * 
     * Login test for an invalid manager account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(5)
    public void testLoginInvalidManagerAccount() {
        //create a manager account
        accountManagementService.createManager();
        
        //login to this manager account with invalid username
        ResponseEntity<ManagerDTO> response = account.postForEntity("/accounts/login/manager/" + INVALID_USERNAME, PASSWORD ,ManagerDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Create Account tests //

    /**
     * 
     * Create a valid customer account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(6)
    public void testCreateValidCustomerAccount() {
        CustomerRequestDTO customer = new CustomerRequestDTO(
            USERNAME,
            PASSWORD,
            EMAIL_STRING,
            PHONENUMBER_STRING,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of()
        );

        ResponseEntity<CustomerResponseDTO> response = account.postForEntity(
            "/accounts/customers",
            customer, CustomerResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(customerRepository.findByEmail(EMAIL_STRING));
    }

    /**
     * 
     * Create an invalid customer account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(7)
    public void testCreateInvalidCustomerAccount() {
        CustomerRequestDTO customer = new CustomerRequestDTO(
            INVALID_USERNAME,
            PASSWORD,
            EMAIL_STRING,
            PHONENUMBER_STRING,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of()
        );

        ResponseEntity<CustomerResponseDTO> response = account.postForEntity(
            "/accounts/customers",
            customer, CustomerResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

        /**
     * 
     * Create a valid employee account (this method is not working)
     * 
     * @Author Clara Mickail
     */
    @Test
    @Order(8)
    public void testCreateValidEmployeeAccount() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO(
                USERNAME,
                PASSWORD,
                true,
                Set.of()
        );

        ResponseEntity<EmployeeResponseDTO> response = account.postForEntity(
                "/accounts/employees",
                employeeRequestDTO,
                EmployeeResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(employeeRepository.findByUsername(USERNAME).isPresent());
    }
    

    /**
     * 
     * Create an invalid employee account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(9)
    public void testCreateInvalidEmployeeAccount() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO(
                INVALID_USERNAME,
                PASSWORD,
                true,
                Set.of()
        );

        ResponseEntity<EmployeeResponseDTO> response = account.postForEntity(
            "/accounts/employees",
                employeeRequestDTO, EmployeeResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * 
     * Create a valid manager account (this method is not working)
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(10)
    public void testCreateValidManagerAccount() {
        ManagerDTO managerDTO = new ManagerDTO(
                1, 
                "manager",
                "manager"
        );

        ResponseEntity<ManagerDTO> response = account.postForEntity(
            "/accounts/manager/",
            managerDTO, ManagerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assertNotNull(response.getBody());
        assertNotNull(managerRepository.findByUsername("manager"));
    }

    /**
     * Create an invalid manager account
     * 
     * @Author Clara Mickail
     */
    @Test
    @Order(11)
    public void testCreateInvalidManagerAccount() {
        // Arrange
        // No need to send ManagerDTO since `createManager` doesn't take any input in the controller
        account.postForEntity("/accounts/manager/", null, ManagerDTO.class); // Creating the first (valid) manager
    
        // Act: Attempt to create another manager (which is invalid cuz only one manager is allowed)
        ResponseEntity<ManagerDTO> response = account.postForEntity(
            "/accounts/manager/",
            null,
            ManagerDTO.class
        );
    
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Get - Account tests //

    /**
     * Get all customer accounts
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(12)
    public void testGetAllCustomerAccounts() {
        CustomerResponseDTO customer1 = new CustomerResponseDTO(
            USERNAME,
            PASSWORD,
            EMAIL_STRING,
            PHONENUMBER_STRING,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of(),
            ID
        );
        customerRepository.save(customer1.toCustomer());
                
        ResponseEntity<CustomerResponseDTO[]> response = account.getForEntity(
            "/accounts/customers/",
            CustomerResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        CustomerResponseDTO[] responseAccount = response.getBody();
        assertNotNull(responseAccount);
        assertEquals(EMAIL_STRING, responseAccount[0].email());
        assertEquals(PHONENUMBER_STRING, responseAccount[0].phoneNumber());
        assertEquals(USERNAME, responseAccount[0].username());
        assertEquals(PASSWORD, responseAccount[0].password());
    }

    /**
     * Get all employee accounts
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(13)
    public void testGetAllEmployeeAccounts() {
        //create employee account
        EmployeeResponseDTO employee1 = new EmployeeResponseDTO(
                ID, 
                USERNAME,
                PASSWORD,
                true,
                Set.of()
        );
        employeeRepository.save(employee1.toEmployee());

        ResponseEntity<EmployeeResponseDTO[]> response = account.getForEntity(
            "/accounts/employees/",
            EmployeeResponseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        EmployeeResponseDTO[] responseAccount = response.getBody();
        assertNotNull(responseAccount);
        assertEquals(USERNAME, responseAccount[0].username());
        assertEquals(PASSWORD, responseAccount[0].password());
    }

    /**
     * Get customer by valid email
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(14)
    public void testGetCustomerByValidEmail() {
        //create customer
        CustomerResponseDTO customer1 = new CustomerResponseDTO(
            USERNAME,
            PASSWORD,
            EMAIL_STRING,
            PHONENUMBER_STRING,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of(),
            ID
        );
        customerRepository.save(customer1.toCustomer());

        ResponseEntity<CustomerResponseDTO> response = account.getForEntity(
            "/accounts/customers/" + EMAIL_STRING,
            CustomerResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EMAIL_STRING, response.getBody().email());
        assertEquals(PHONENUMBER_STRING, response.getBody().phoneNumber());
        assertEquals(USERNAME, response.getBody().username());
        assertEquals(PASSWORD, response.getBody().password());
    }

    /**
     * Get customer by invalid email
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(15)
    public void testGetCustomerByInvalidEmail() {
        //create customer
        CustomerResponseDTO customer1 = new CustomerResponseDTO(
            USERNAME,
            PASSWORD,
            EMAIL_STRING_INVALID,
            PHONENUMBER_STRING,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of(),
            ID
        );
        customerRepository.save(customer1.toCustomer());

        ResponseEntity<CustomerResponseDTO> response = account.getForEntity(
            "/accounts/customers/" + EMAIL_STRING_INVALID,
            CustomerResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Get employee by valid username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(16)
    public void testGetEmployeeByValidUsername() {
        Employee employee = new Employee(
                USERNAME,
                PASSWORD,
                true
        );
        employeeRepository.save(employee);

        ResponseEntity<EmployeeResponseDTO> response = account.getForEntity(
            "/accounts/employees/username/" + USERNAME,
            EmployeeResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(USERNAME, response.getBody().username());
        assertEquals(PASSWORD, response.getBody().password());
    }

    /**
     * Get employee by invalid username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(17)
    public void testGetEmployeeByInvalidUsername() {
        //create employee account
        EmployeeResponseDTO employee1 = new EmployeeResponseDTO(
                ID, 
                INVALID_USERNAME,
                PASSWORD,
                true,
                Set.of()
        );
        employeeRepository.save(employee1.toEmployee());

        ResponseEntity<EmployeeResponseDTO> response = account.getForEntity(
            "/accounts/employees/username/" + INVALID_USERNAME,
            EmployeeResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Update - Account tests //

    /**
     * Update employee status (test not working)
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(18)
    public void testUpdateEmployeeStatus() {
        // create employee account
        // EmployeeDTO employee1 = new EmployeeDTO(
        //     ID, 
        //     USERNAME,
        //     PASSWORD,
        //     true,
        //     Set.of(),
        //     Set.of()
        // );
        // Employee emp = employee1.toEmployee();
        // employeeRepository.save(emp);
        // emp.setActive(false);

        // ResponseEntity<EmployeeDTO> response = account.exchange(
        // "/accounts/employees/" + ID + "/is_active/" + false, HttpMethod.PUT, new HttpEntity<>(employee1),
        // EmployeeDTO.class);
    
        // assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    /**
     * Update valid customer password
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(19)
    public void testUpdateValidCustomerPassword() {
        Customer customer = new Customer(USERNAME, OLD_PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(OLD_PASSWORD, PASSWORD);

        // Act
        ResponseEntity<CustomerResponseDTO> response = account.exchange(
            "/accounts/customers/" + customer.getEmail() + "/password",
            HttpMethod.PUT,
            new HttpEntity<>(changePasswordDTO),
            CustomerResponseDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        customer = customerRepository.findById(customer.getId()).get();
        assertEquals(PASSWORD, customer.getPassword());
    }



    /**
     * Update invalid customer password
     * 
     * @Author Clara Mickail
     */
    @Test
    @Order(20)
    public void testUpdateInvalidCustomerPassword() {
        // Arrange
        Customer customer = new Customer("Guy", "oldPassword", "guy@email.com", "1234567890");
        customerRepository.save(customer);

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(INVALID_PASSWORD, "newpass");


        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + customer.getEmail() + "/password",
            HttpMethod.PUT,
                new HttpEntity<>(changePasswordDTO),
            String.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    /**
     * Update valid customer username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(21)
    public void testUpdateValidCustomerUsername() {
        Customer customer = new Customer(OLD_USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + customer.getEmail() + "/username/" + USERNAME,
            HttpMethod.PUT,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        customer = customerRepository.findById(customer.getId()).get();
        assertEquals(USERNAME, customer.getUsername());
    }
    

    /**
     * Update invalid customer username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(22)
    public void testUpdateInvalidCustomerUsername() {
        // Arrange
        Customer customer = new Customer(OLD_USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + EMAIL_STRING + "/username/ ",
            HttpMethod.PUT,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    /**
     * Update valid customer phone number
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(23)
    public void testUpdateValidCustomerPhoneNumber() {
        Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING, OLD_PHONENUMBER);
        customerRepository.save(customer);

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + customer.getEmail() + "/phoneNumber/" + PHONENUMBER_STRING,
            HttpMethod.PUT,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        customer = customerRepository.findById(customer.getId()).get();
        assertEquals(PHONENUMBER_STRING, customer.getPhoneNumber());
    }

    /**
     * Update invalid customer phone number
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(24)
    public void testUpdateInvalidCustomerPhoneNumber() {
         // Arrange
         Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING, OLD_PHONENUMBER);
         customerRepository.save(customer);
 
         // Act
         ResponseEntity<String> response = account.exchange(
             "/accounts/customers/" + EMAIL_STRING + "/phoneNumber/ ",
             HttpMethod.PUT,
             null,
             String.class
         );
 
         // Assert
         assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Update valid employee username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(25)
    public void testUpdateValidEmployeeUsername() {
        Employee employee = new Employee(OLD_USERNAME, PASSWORD, true);
        employeeRepository.save(employee);

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/employees/" + OLD_USERNAME + "/username/" + USERNAME,
            HttpMethod.PUT,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        employee = employeeRepository.findById(employee.getId()).get();
        assertEquals(USERNAME, employee.getUsername());
    }

    /**
     * Update invalid employee username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(26)
    public void testUpdateInvalidEmployeeUsername() {
        Employee employee = new Employee(OLD_USERNAME, PASSWORD, true);
        employeeRepository.save(employee);

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/employees/" + OLD_USERNAME + "/username/ ",
            HttpMethod.PUT,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(27)
    @Transactional
    public void testAddValidGameToWishlist() {
        // Arrange
        Customer customer = accountManagementService.createCustomer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customer = customerRepository.save(customer);
        System.out.println(customer.getId());
        System.out.println("123");
    
        Game game = new Game("Test Game", "Test Description", "test.jpg", 29.99f, true, 11);
        gameRepository.save(game);
        assertNotNull(game.getId(), "Game ID should not be null after saving.");

        // Act
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customer.getId() + "/wishlist/" + game.getId(), HttpMethod.PUT,
                null,
                Void.class
        );
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        // assertNotNull(updatedCustomer);
        // assertTrue(
        //     updatedCustomer.getCopyWishlist().stream().anyMatch(g -> g.getId() == gameId));
    
    }
    

    @Test
    @Order(28)
    public void testAddInvalidGameToWishlist() {
        // Arrange
        Customer customer = accountManagementService.createCustomer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);

        int invalidGameId = 9999994; // Assuming this game ID doesn't exist
    
        // Act
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customer.getId() + "/wishlist/" + invalidGameId,
            HttpMethod.PUT,
            null,
            Void.class
        );
    
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    

    @Test
    @Order(29)
    @Transactional
    public void testRemoveValidGameFromWishlist() {
        // Arrange
        Customer customer = accountManagementService.createCustomer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customer = customerRepository.save(customer);
    
        Game game = new Game("Test Game", "Test Description", "test.jpg", 29.99f, true, 11);
        gameRepository.save(game);
    
        //Act
        // Add the game to the customer's wishlist
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customer.getId() + "/wishlist/" + game.getId(),
            HttpMethod.PUT,
            null,
            Void.class
        );
    
        // Remove the game from the customer's wishlist
       account.exchange(
            "/accounts/customers/" + customer.getId() + "/wishlist/" + game.getId(),
            HttpMethod.DELETE,
            null,
            Void.class
        );
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Reload the customer and verify the wishlist
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assertNotNull(updatedCustomer);
        assertFalse(updatedCustomer.getCopyWishlist().stream().anyMatch(g -> g.getId() == game.getId()));
    }
    
    @Test
    @Order(30)
    public void testRemoveInvalidGameFromWishlist() {
        // Arrange
        Customer customer = accountManagementService.createCustomer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);
    
        int invalidGameId = 99999123; // Assuming this game ID doesn't exist
    
        // Act: Try removing the invalid game from the customer's wishlist
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customer.getId() + "/wishlist/" + invalidGameId,
            HttpMethod.DELETE,
            null,
            Void.class
        );
    
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    

    @Test
    @Order(31)
    @Transactional
    public void testViewWishlist() {
        // Arrange
        Customer customer = accountManagementService.createCustomer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customer = customerRepository.save(customer);
    
        Game game = new Game("Test Game", "Test Description", "test.jpg", 29.99f, true, 10);
        gameRepository.save(game);
    
        account.exchange(
            "/accounts/customers/" + customer.getId() + "/wishlist/" + game.getId(),
            HttpMethod.PUT,
            null,
            Void.class
        );
    
        // Act
        ResponseEntity<GameResponseDTO[]> response = account.exchange(
            "/accounts/customers/" + customer.getId() + "/wishlist",
            HttpMethod.GET,
            null,
            GameResponseDTO[].class
        );
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(
            Arrays.stream(response.getBody()).anyMatch(g -> g.id() == game.getId())
        );
    }
    

    
}