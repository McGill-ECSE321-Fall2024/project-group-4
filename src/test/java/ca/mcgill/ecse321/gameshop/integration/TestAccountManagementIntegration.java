package ca.mcgill.ecse321.gameshop.integration;

import ca.mcgill.ecse321.gameshop.DAO.AccountRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;
import ca.mcgill.ecse321.gameshop.DAO.ManagerRepository;
import ca.mcgill.ecse321.gameshop.dto.CustomerDTO;
import ca.mcgill.ecse321.gameshop.dto.EmployeeDTO;
import ca.mcgill.ecse321.gameshop.dto.ManagerDTO;
import ca.mcgill.ecse321.gameshop.model.Account;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.model.Manager;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private int ID = 0;
    private int INVALID_ID = -1;
    private String INVALID_USERNAME = "testUsername Not Valid";
    private String INVALID_PASSWORD = "testPassword Not Valid";
    private String OLD_USERNAME = "testUsername_old";
    private String OLD_PASSWORD = "testPassword_old";
    private String OLD_PHONENUMBER = "1234567890";

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
        ResponseEntity<CustomerDTO> response = account.postForEntity("/accounts/login/customers/" + EMAIL_STRING, PASSWORD ,CustomerDTO.class);


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
        ResponseEntity<CustomerDTO> response = account.postForEntity("/accounts/login/customers/" + EMAIL_STRING_INVALID, PASSWORD ,CustomerDTO.class);

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
        ResponseEntity<EmployeeDTO> response = account.postForEntity("/accounts/login/employees/" + USERNAME, PASSWORD ,EmployeeDTO.class);

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
        ResponseEntity<EmployeeDTO> response = account.postForEntity("/accounts/login/employees/" + INVALID_USERNAME, PASSWORD ,EmployeeDTO.class);

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
        // Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        // customerRepository.save(customer);
        CustomerDTO customer = new CustomerDTO(
            USERNAME,
            PASSWORD,
            EMAIL_STRING,
            PHONENUMBER_STRING,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of(),
            1
        );


        // List<Customer> customers = (List<Customer>) customerRepository.findAll();
        // List<CustomerDTO> customerDtos = CustomerDTO.convertToDto(customers);

        ResponseEntity<CustomerDTO> response = account.postForEntity(
            "/accounts/customers/" + EMAIL_STRING,
            customer, CustomerDTO.class);

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
        // Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING_INVALID, PHONENUMBER_STRING);
        // customerRepository.save(customer);
        CustomerDTO customer = new CustomerDTO(
            INVALID_USERNAME,
            PASSWORD,
            EMAIL_STRING,
            PHONENUMBER_STRING,
            Set.of(),
            Set.of(),
            Set.of(),
            Set.of(),
            1
        );

        ResponseEntity<CustomerDTO> response = account.postForEntity(
            "/accounts/customers/" + EMAIL_STRING,
            customer, CustomerDTO.class);

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
        EmployeeDTO employeeDTO = new EmployeeDTO(
                0, 
                USERNAME,
                PASSWORD,
                true,
                Set.of(),
                Set.of()
        );

        ResponseEntity<EmployeeDTO> response = account.postForEntity(
                "/accounts/employees/" + USERNAME,
                employeeDTO, 
                EmployeeDTO.class
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
        EmployeeDTO employeeDTO = new EmployeeDTO(
                0, 
                INVALID_USERNAME,
                PASSWORD,
                true,
                Set.of(),
                Set.of()
        );

        ResponseEntity<EmployeeDTO> response = account.postForEntity(
            "/accounts/employees/" + INVALID_USERNAME,
            employeeDTO, EmployeeDTO.class);

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
        CustomerDTO customer1 = new CustomerDTO(
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
                
        ResponseEntity<CustomerDTO[]> response = account.getForEntity(
            "/accounts/customers/",
            CustomerDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        CustomerDTO[] responseAccount = response.getBody();
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
        EmployeeDTO employee1 = new EmployeeDTO(
                ID, 
                USERNAME,
                PASSWORD,
                true,
                Set.of(),
                Set.of()
        );
        employeeRepository.save(employee1.toEmployee());

        ResponseEntity<EmployeeDTO[]> response = account.getForEntity(
            "/accounts/employees/",
            EmployeeDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        EmployeeDTO[] responseAccount = response.getBody();
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
        CustomerDTO customer1 = new CustomerDTO(
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

        ResponseEntity<CustomerDTO> response = account.getForEntity(
            "/accounts/customers/" + EMAIL_STRING,
            CustomerDTO.class);

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
        CustomerDTO customer1 = new CustomerDTO(
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

        ResponseEntity<CustomerDTO> response = account.getForEntity(
            "/accounts/customers/" + EMAIL_STRING_INVALID,
            CustomerDTO.class);

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
        //create employee account
        EmployeeDTO employee1 = new EmployeeDTO(
                ID, 
                USERNAME,
                PASSWORD,
                true,
                Set.of(),
                Set.of()
        );
        employeeRepository.save(employee1.toEmployee());

        ResponseEntity<EmployeeDTO> response = account.getForEntity(
            "/accounts/employees/" + USERNAME,
            EmployeeDTO.class);

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
        EmployeeDTO employee1 = new EmployeeDTO(
                ID, 
                INVALID_USERNAME,
                PASSWORD,
                true,
                Set.of(),
                Set.of()
        );
        employeeRepository.save(employee1.toEmployee());

        ResponseEntity<EmployeeDTO> response = account.getForEntity(
            "/accounts/employees/" + INVALID_USERNAME,
            EmployeeDTO.class);

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

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + customer.getEmail() + "/password",
            HttpMethod.PUT,
            new HttpEntity<>(Map.of(OLD_PASSWORD, OLD_PASSWORD, PASSWORD, PASSWORD)),
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
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

        // Act
        ResponseEntity<String> response = account.exchange(
            "/accounts/customers/" + customer.getEmail() + "/password",
            HttpMethod.PUT,
            new HttpEntity<>(Map.of("oldPassword", "wrongOldPassword", "newPassword", "newPassword1")),
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
            "/accounts/customers/" + customer.getEmail() + "/password",
            HttpMethod.PUT,
            new HttpEntity<>(Map.of(OLD_USERNAME, OLD_USERNAME, USERNAME, USERNAME)),
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
            "/accounts/customers/" + customer.getEmail() + "/username",
            HttpMethod.PUT,
            new HttpEntity<>(""),
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
            "/accounts/customers/" + customer.getEmail() + "/phoneNumber",
            HttpMethod.PUT,
            new HttpEntity<>(Map.of(OLD_PHONENUMBER, OLD_PHONENUMBER, PHONENUMBER_STRING, PHONENUMBER_STRING)),
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
             "/accounts/customers/" + customer.getEmail() + "/username",
             HttpMethod.PUT,
             new HttpEntity<>(""),
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
            "/accounts/employees/" + OLD_USERNAME,
            HttpMethod.PUT,
            new HttpEntity<>(Map.of(OLD_USERNAME, OLD_USERNAME, USERNAME, USERNAME)),
            String.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
            "/accounts/employees/" + OLD_USERNAME,
            HttpMethod.PUT,
            new HttpEntity<>(""),
            String.class
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(27)
    public void testAddValidGameToWishlist() {
    }

    @Test
    @Order(28)
    public void testAddInvalidGameToWishlist(){

    }

    @Test
    @Order(29)
    public void testRemoveValidGameFromWishlist(){

    }

    @Test
    @Order(30)
    public void testRemoveInvalidGameFromWishlist(){

    }

    @Test
    @Order(31)
    public void testViewWishlist(){

    }

    
}