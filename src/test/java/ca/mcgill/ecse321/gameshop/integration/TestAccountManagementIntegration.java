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
import ca.mcgill.ecse321.gameshop.dto.CustomerDTOConverter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

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
        Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);

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
        Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING_INVALID, PHONENUMBER_STRING);
        customerRepository.save(customer);

        ResponseEntity<CustomerDTO> response = account.postForEntity(
            "/accounts/customers/" + EMAIL_STRING,
            customer, CustomerDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * 
     * Create a valid employee account (this method is not working)
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(8)
    public void testCreateValidEmployeeAccount() {
        Employee employee = new Employee(USERNAME, PASSWORD, true);
        employeeRepository.save(employee);

        ResponseEntity<EmployeeDTO> response = account.postForEntity(
            "/accounts/employees/" + USERNAME,
            employee, EmployeeDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(employeeRepository.findByUsername(USERNAME));
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
        Employee employee = new Employee(INVALID_USERNAME, PASSWORD, true);
        employeeRepository.save(employee);

        ResponseEntity<EmployeeDTO> response = account.postForEntity(
            "/accounts/employees/" + USERNAME,
            employee, EmployeeDTO.class);

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
        Manager manager = new Manager();
        managerRepository.save(manager);

        ResponseEntity<ManagerDTO> response = account.postForEntity(
            "/accounts/manager/",
            account, ManagerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assertNotNull(response.getBody());
        assertNotNull(managerRepository.findByUsername("manager"));
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
        Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);

        ResponseEntity<CustomerDTO> response = account.getForEntity(
            "/accounts/customers/",
            CustomerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // CustomerDTO[] responseAccount = response.getBody();
        // assertNotNull(responseAccount);
        // assertEquals(EMAIL_STRING, responseAccount[0].email());
        // assertEquals(PHONENUMBER_STRING, responseAccount[0].phoneNumber());
        // assertEquals(USERNAME, responseAccount[0].username());
        // assertEquals(PASSWORD, responseAccount[0].password());
    }

    /**
     * Get all employee accounts
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(12)
    public void testGetAllEmployeeAccounts() {

        //create employee account
        Employee employee = new Employee(USERNAME, PASSWORD, true);
        employeeRepository.save(employee);

        ResponseEntity<EmployeeDTO> response = account.getForEntity(
            "/accounts/employees/",
            EmployeeDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // EmployeeDTO[] responseAccount = response.getBody();
        // assertNotNull(responseAccount);
        // assertEquals(USERNAME, responseAccount[0].username());
        // assertEquals(PASSWORD, responseAccount[0].password());
    }

    /**
     * Get customer by valid email
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(13)
    public void testGetCustomerByValidEmail() {
        Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING, PHONENUMBER_STRING);
        customerRepository.save(customer);

        ResponseEntity<CustomerDTO> response = account.getForEntity(
            "/accounts/customers/" + EMAIL_STRING,
            CustomerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // assertEquals(EMAIL_STRING, response.getBody().email());
        // assertEquals(PHONENUMBER_STRING, response.getBody().phoneNumber());
        // assertEquals(USERNAME, response.getBody().username());
        // assertEquals(PASSWORD, response.getBody().password());
    }

    /**
     * Get customer by invalid email
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(14)
    public void testGetCustomerByInvalidEmail() {
        Customer customer = new Customer(USERNAME, PASSWORD, EMAIL_STRING_INVALID, PHONENUMBER_STRING);
        customerRepository.save(customer);

        ResponseEntity<CustomerDTO> response = account.getForEntity(
            "/accounts/customers/" + EMAIL_STRING,
            CustomerDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Get employee by valid username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(15)
    public void testGetEmployeeByValidUsername() {
        Employee employee = new Employee(USERNAME, PASSWORD, true);
        employeeRepository.save(employee);

        ResponseEntity<EmployeeDTO> response = account.getForEntity(
            "/accounts/employees/" + USERNAME,
            EmployeeDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // assertEquals(USERNAME, response.getBody().username());
        // assertEquals(PASSWORD, response.getBody().password());
    }

    /**
     * Get employee by invalid username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(16)
    public void testGetEmployeeByInvalidUsername() {
        Employee employee = new Employee(INVALID_USERNAME, PASSWORD, true);
        employeeRepository.save(employee);

        ResponseEntity<EmployeeDTO> response = account.getForEntity(
            "/accounts/employees/" + USERNAME,
            EmployeeDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Update - Account tests //

    /**
     * Update employee status
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(17)
    public void testUpdateEmployeeStatus() {
        // insert here, use put
    }

    /**
     * Update valid customer password
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(18)
    public void testUpdateValidCustomerPassword() {
    }

    /**
     * Update invalid customer password
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(19)
    public void testUpdateInvalidCustomerPassword() {
    }

    /**
     * Update valid customer username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(20)
    public void testUpdateValidCustomerUsername() {
    }

    /**
     * Update invalid customer username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(21)
    public void testUpdateInvalidCustomerUsername() {
    }

    /**
     * Update valid customer phone number
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(22)
    public void testUpdateValidCustomerPhoneNumber() {
    }

    /**
     * Update invalid customer phone number
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(23)
    public void testUpdateInvalidCustomerPhoneNumber() {
    }

    /**
     * Update valid employee username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(24)
    public void testUpdateValidEmployeeUsername() {
    }

    /**
     * Update invalid employee username
     * 
     * @Author Ana Gordon
     */
    @Test
    @Order(25)
    public void testUpdateInvalidEmployeeUsername() {
    }

    
}