package ca.mcgill.ecse321.gameshop.integration;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.controller.GameManagementController;
import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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

    @Autowired
    private GameManagementController gameController;

    private String USERNAME = "testUsernameValid";
    private String PASSWORD = "testPasswordValid";
    private String EMAIL_STRING = "emailvalid@email.com";
    private String EMAIL_STRING_INVALID = "invalid email";
    private String PHONENUMBER_STRING = "0123456789";

    private final String employeeUsername = "employee";
    private final String employeePassword = "safePassword";
    private int employeeID = 0;

    private int customerID = 0;
    private String INVALID_USERNAME = "testUsername Not Valid";
    private String INVALID_PASSWORD = "testPassword Not Valid";
    private String OLD_USERNAME = "testUsername_old";
    private String OLD_PHONENUMBER = "1234567890";
    private static final String TEST_CATEGORY_NAME1 = "test category name1";
    private int gameId;

    @Autowired
    private AccountManagementService accountManagementService;

    
//    @BeforeEach
//    @AfterEach
//    public void clearDatabase() {
//        customerRepository.deleteAll();
//        employeeRepository.deleteAll();
//        managerRepository.deleteAll();
//        accountRepository.deleteAll();
//        gameRepository.deleteAll();
//    }

    @AfterAll
    public void clear() {
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        accountRepository.deleteAll();
        gameRepository.deleteAll();
    }



    @Test
    @Order(0)
    public void testCreateCustomerAccount() {
        //Arrange
        Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        CustomerRequestDTO customerDto = new CustomerRequestDTO(customer);


        //Act
        ResponseEntity<CustomerResponseDTO> response = account.postForEntity("/accounts/customers", customerDto, CustomerResponseDTO.class);


        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(USERNAME, response.getBody().username());
        customerID = response.getBody().id();
    }



    /**
     * 
     * Login test for a valid customer account
     * 
     * @author Ana Gordon
     */
    @Test
    @Order(1)
    public void testLoginValidCustomerAccount() {
        //login to a customer account
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
    @Order(2)
    public void testLoginInvalidCustomerAccount() {

        //login to this customer account with invalid email
        ResponseEntity<CustomerResponseDTO> response = account.postForEntity("/accounts/login/customers/" + EMAIL_STRING_INVALID, PASSWORD , CustomerResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    /**
     * @Author Tarek Namani
     * Test the creation of an employee account
     */
    @Test
    @Order(3)
    public void testCreateEmployeeAccount() {
        //Arrange
        Employee employee = new Employee(employeeUsername,employeePassword,true);
        EmployeeRequestDTO employeeDto = new EmployeeRequestDTO(employee);


        //Act
        ResponseEntity<EmployeeResponseDTO> response = account.postForEntity("/accounts/employees", employeeDto, EmployeeResponseDTO.class);


        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isActive());
        employeeID = response.getBody().id();

    }

    /**
     * 
     * Login test for a valid employee account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(4)
    public void testLoginValidEmployeeAccount() {
        //login to this employee account
        ResponseEntity<EmployeeResponseDTO> response = account.postForEntity("/accounts/login/employees/" + employeeUsername,employeePassword , EmployeeResponseDTO.class);

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
    @Order(5)
    public void testLoginInvalidEmployeeAccount() {
        //login to an employee account with invalid username
        ResponseEntity<EmployeeResponseDTO> response = account.postForEntity("/accounts/login/employees/" + INVALID_USERNAME, PASSWORD , EmployeeResponseDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    /**
     * Test the creation of a manager account
     * @author Tarek Namani
     */
    @Test
    @Order(6)
    public void testCreateManagerAccount() {
        //Act
        ResponseEntity<ManagerDTO> response = account.postForEntity("/accounts/manager/", null,ManagerDTO.class);


        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("manager", response.getBody().username());
    }


    /**
     * 
     * Login test for a valid manager account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(7)
    public void testLoginValidManagerAccount() {

        
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
    @Order(8)
    public void testLoginInvalidManagerAccount() {
        //login to manager account with invalid username
        ResponseEntity<ManagerDTO> response = account.postForEntity("/accounts/login/manager/" + INVALID_USERNAME, PASSWORD ,ManagerDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * 
     * Create an invalid customer account
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(9)
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
     * Create an invalid manager account
     * 
     * @Author Clara Mickail
     */
    @Test
    @Order(10)
    public void testCreateInvalidManagerAccount() {
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
    @Order(11)
    public void testGetAllCustomerAccounts() {
                
        ResponseEntity<Set<CustomerResponseDTO>> response = account.exchange("/accounts/customers/", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Set<CustomerResponseDTO> customers = response.getBody();
        assertNotNull(customers);
        assertEquals(EMAIL_STRING, customers.iterator().next().email());
        assertEquals(PHONENUMBER_STRING, customers.iterator().next().phoneNumber());
        assertEquals(USERNAME, customers.iterator().next().username());
        assertEquals(PASSWORD, customers.iterator().next().password());
    }

    /**
     * Get all employee accounts
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(13)
    public void testGetAllEmployeeAccounts() {

        ResponseEntity<Set<EmployeeResponseDTO>> response = account.exchange("/accounts/employees/", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Set<EmployeeResponseDTO> employees = response.getBody();
        assertNotNull(employees);
        assertEquals(employeeUsername, employees.iterator().next().username());
        assertEquals(employeePassword, employees.iterator().next().password());
    }

    /**
     * Get customer by valid email
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(14)
    public void testGetCustomerByValidEmail() {
        //Act
        ResponseEntity<CustomerResponseDTO> response = account.getForEntity("/accounts/customers/" + EMAIL_STRING, CustomerResponseDTO.class);

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

        ResponseEntity<EmployeeResponseDTO> response = account.getForEntity(
            "/accounts/employees/username/" + employeeUsername,
            EmployeeResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employeeUsername, response.getBody().username());
        assertEquals(employeePassword, response.getBody().password());
    }

    /**
     * Get employee by invalid username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(17)
    public void testGetEmployeeByInvalidUsername() {
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

         ResponseEntity<Void> response = account.exchange("/accounts/employees/" + employeeID + "/is_active/" + false, HttpMethod.PUT,null, Void.class);
    
         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertFalse(employeeRepository.findById(employeeID).get().isActive());

         //Reset to active
        account.exchange("/accounts/employees/" + employeeID + "/is_active/" + true, HttpMethod.PUT,null, Void.class);

    }

    /**
     * Update valid customer password
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(19)
    public void testUpdateValidCustomerPassword() {
        //Arrange
        String newPassword = "saferPassword";
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(PASSWORD, newPassword);

        // Act
        ResponseEntity<CustomerResponseDTO> response = account.exchange(
            "/accounts/customers/" + EMAIL_STRING + "/password",
            HttpMethod.PUT,
            new HttpEntity<>(changePasswordDTO),
            CustomerResponseDTO.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer customer = customerRepository.findById(customerID).get();
        assertEquals(newPassword, customer.getPassword());
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
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(INVALID_PASSWORD, "newpass");


        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + EMAIL_STRING_INVALID + "/password",
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
        //Arrange
        String newUsername = "newUsername";

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + EMAIL_STRING + "/username/"+newUsername,
            HttpMethod.PUT,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer customer = customerRepository.findById(customerID).get();
        assertEquals(newUsername, customer.getUsername());
    }
    

    /**
     * Update invalid customer username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(22)
    public void testUpdateInvalidCustomerUsername() {

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
        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + EMAIL_STRING + "/phoneNumber/" + PHONENUMBER_STRING,
            HttpMethod.PUT,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer customer = customerRepository.findById(customerID).get();
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
        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/employees/" + employeeUsername + "/username/" + " ",
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
        GameInputDTO gameDTO = new GameInputDTO("Test Game", "Test Description", "test.jpg", 29.99f, true, 11,new ArrayList<>());
        gameId = account.postForEntity("/games", gameDTO, GameResponseDTO.class).getBody().id();

        // Act
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customerID + "/wishlist/" + gameId,
            HttpMethod.PUT,
            null,  
            Void.class
        );
   
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());  

        // Reload and assert
        Customer customer1 = customerRepository.findById(customerID).get();
        assertTrue(customer1.getCopyWishlist().stream().map(game -> game.getName()).anyMatch( name -> name.equals("Test Game")));
    }
    

    @Test
    @Order(28)
    public void testAddInvalidGameToWishlist() {
        // Arrange
        int invalidGameId = 9999994; // Assuming this game ID doesn't exist
    
        // Act
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customerID + "/wishlist/" + invalidGameId,
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
    
        //Act
        // Add the game to the customer's wishlist
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customerID + "/wishlist/" + gameId,
            HttpMethod.PUT,
            null,
            Void.class
        );
    
        // Remove the game from the customer's wishlist
       account.exchange(
            "/accounts/customers/" + customerID + "/wishlist/" + gameId,
            HttpMethod.DELETE,
            null,
            Void.class
        );
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    
        // Reload
        Customer updatedCustomer = customerRepository.findById(customerID).orElse(null);
        assertNotNull(updatedCustomer);
        assertFalse(updatedCustomer.getCopyWishlist().stream().anyMatch(g -> g.getId() == gameId));
    }
    
    @Test
    @Order(30)
    public void testRemoveInvalidGameFromWishlist() {
        // Arrange
    
        int invalidGameId = 99999123; // Assuming this game ID doesn't exist
    
        // Act: Try removing the invalid game from the customer's wishlist
        ResponseEntity<Void> response = account.exchange(
            "/accounts/customers/" + customerID + "/wishlist/" + invalidGameId,
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
        account.exchange(
            "/accounts/customers/" + customerID + "/wishlist/" + gameId,
            HttpMethod.PUT,
            null,
            Void.class
        );
        String uri = "/accounts/customers/" + customerID + "/wishlist";
        // Act
        ResponseEntity<List<GameResponseDTO>> response = account.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<GameResponseDTO> wishlish = response.getBody();
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(wishlish.stream().anyMatch(gameResponseDTO -> gameResponseDTO.id() == gameId));
    }
}