package ca.mcgill.ecse321.gameshop.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.EntityNotFoundException;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

/**
 * Unit test for AccountManagementService
 * 
 * @author Ana Gordon, Tarek Namani, Clara Mickail
 */
@SpringBootTest
public class TestAccountManagementService {

    @InjectMocks
    private AccountManagementService accountManagementService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AddressRepository addressRepository;

    // Initialize the examples
    Account account1;
    Account account2;
    Customer customer1;
    Customer customer2;
    Employee employee1;
    Employee employee2;
    Manager manager;
    Set<Customer> customers = new HashSet<>();
    Set<Employee> employees = new HashSet<>();
    private Customer referenceCustomer;
    private Game referenceGame;


    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
    }
    
    @BeforeEach
    public void setUp() {
        referenceCustomer = new Customer("testUser", "password", "test@gmail.com", "1234567890");
        referenceGame = new Game("Test Game", "Test Description", "Test Cover", 50.0f, true, 10);

        when(customerRepository.findById(referenceCustomer.getId())).thenReturn(Optional.of(referenceCustomer));
        when(gameRepository.findById(referenceGame.getId())).thenReturn(Optional.of(referenceGame));

        //initialize all fields
        customer1 = new Customer("customer1", "password1", "customer1@email.com", "0123456789");
        customer2 = new Customer("customer2", "password2", "customer2@email.com", "012ad3456789");
        employee1 = new Employee("employee1", "password1", true);
        employee2 = new Employee("employee2", "password2", false);
        manager = new Manager("manager", "manager");

        when(accountRepository.save(any(Account.class))).thenReturn(account1);
        when(accountRepository.findByUsername(anyString())).thenReturn(account1);
        when(accountRepository.findAccountById(anyInt())).thenReturn(account1);
        when(accountRepository.findByUsername("invalidUsername")).thenReturn(null);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer1);
        when(customerRepository.findCustomerById(0)).thenReturn(Optional.of(customer1));

        when(customerRepository.findCustomerById(-1)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(customer1.getEmail())).thenReturn(Optional.of(customer1));
        when(customerRepository.findByEmail(customer2.getEmail())).thenReturn(Optional.of(customer2));
        when(customerRepository.findByEmail("uniqueEmail")).thenReturn(Optional.empty());
        customers.add(customer1);
        customers.add(customer2);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerRepository.findByEmail("invalidEmail")).thenReturn(Optional.empty());
        when(customerRepository.save(customer1)).thenReturn(customer1);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);
        when(employeeRepository.findById(0)).thenReturn(Optional.of(employee1));
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee2));
//        when(employeeRepository.findById(-1)).thenReturn(Optional.empty());
        when(employeeRepository.findByUsername("uniqueUsername")).thenReturn(Optional.empty());
        when(employeeRepository.findByUsername("employee1")).thenReturn(Optional.of(employee1));
        when(employeeRepository.findByUsername("employee2")).thenReturn(Optional.of(employee2));
        employees.add(employee1);
        employees.add(employee2);
        when(employeeRepository.findAll()).thenReturn(employees);


        when(managerRepository.save(any(Manager.class))).thenReturn(manager);
        when(managerRepository.findByUsername("manager")).thenReturn(Optional.of(manager));
        when(managerRepository.findAll()).thenReturn(Set.of(manager));
    }

    /**
     * Test for creating a customer account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomer(){
        //Arrange
        String email = "uniqueEmail";
        String password = "newPassword";
        String username = "newUsername";
        String phoneNumber = "newPhoneNumber";


        // Act
        Customer customer = accountManagementService.createCustomer(email, password, username, phoneNumber);

        // Assert
        assertNotNull(customer);
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(username, customer.getUsername());
        assertEquals(phoneNumber, customer.getPhoneNumber());
        verify(customerRepository, times(1)).save(customer);
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerEmailNull(){
        //Arrange
        String email = null;
        String password = "newPassword";
        String username = "newUsername";
        String phoneNumber = "newPhoneNumber";


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.createCustomer(email,password,username,phoneNumber)) ;

        // Assert
       assertEquals(exception.getMessage(),"Email cannot be empty, null, or contain spaces.");
    }

    /**
     * Tets for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerPhoneNumberInvalid(){
        //Arrange
        String email = "uniqueEmail";
        String password = "newPassword";
        String username = "newUsername";
        String phoneNumber = null;


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.createCustomer(email,password,username,phoneNumber)) ;

        // Assert
        assertEquals(exception.getMessage(),"Phone number cannot be empty, null, or contain spaces.");
    }

    /**
     * Tets for creating a customer account, fail
     *
     * @author Tarek Namani
     */
    @Test
    public void testCreateCustomerPasswordInvalid(){
        //Arrange
        String email = "uniqueEmail";
        String password = " ";
        String username = "newUsername";
        String phoneNumber = "goodPhoneNumber";


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.createCustomer(email,password,username,phoneNumber)) ;

        // Assert
        assertEquals(exception.getMessage(),"Password cannot be empty, null, or contain spaces.");
    }


    /**
     * Tets for creating a customer account, fail
     *
     * @author Tarek Namani
     */
    @Test
    public void testCreateCustomerWithInvalidUsername(){
        //Arrange
        String email = "uniqueEmail";
        String password = "newPassword";
        String username = "";
        String phoneNumber = "validPhoneNumber";


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.createCustomer(email,password,username,phoneNumber)) ;

        // Assert
        assertEquals(exception.getMessage(),"Username cannot be empty, null, or contain spaces.");
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerEmailAlreadyExists(){
        //Arrange
        String email = "customer1@email.com";
        String password = "newPassword";
        String username = "validPassword";
        String phoneNumber = "validPhoneNumber";


        // Act
        EntityExistsException exception = assertThrows(EntityExistsException.class, ()->accountManagementService.createCustomer(email,password,username,phoneNumber)) ;

        // Assert
        assertEquals(exception.getMessage(),"Customer with this email already exists.");
    }

    /**
     * Test for creating an employee account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateEmployee(){
        //Arrange
        String username = "uniqueUsername";
        String password = "validPassword";
        Boolean isActive = true;

        //Act
        Employee employee = accountManagementService.createEmployee(username, password, isActive);

        //Assert
        assertNotNull(employee);
        assertEquals(username, employee.getUsername());
        assertEquals(password, employee.getPassword());
        assertEquals(isActive, employee.isActive());
        verify(employeeRepository, times(1)).save(employee);
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateInactiveEmployee(){
        //Arrange
        String username = "uniqueUsername";
        String password = "aSafePassword";
        boolean isActive = false;

        // Act
        Employee employee = accountManagementService.createEmployee(username, password, isActive);
        // Assert
        assertNotNull(employee);
        assertEquals(username, employee.getUsername());
        assertEquals(password, employee.getPassword());
        assertEquals(isActive, employee.isActive());
        verify(employeeRepository, times(1)).save(employee);
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateEmployeeUsernameNull(){
        //Arrange
        String username = " ";
        String password = "aSafePassword";
        boolean isActive = false;

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.createEmployee(username, password, isActive)) ;
        // Assert
       assertEquals(exception.getMessage(),"Username cannot be empty, null, or contain spaces.");
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateEmployeePasswordNull(){
        //Arrange
        String username = "validUsername";
        String password = "aSafePa ssword";
        boolean isActive = false;

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.createEmployee(username, password, isActive)) ;
        // Assert
        assertEquals(exception.getMessage(),"Password cannot be empty, null, or contain spaces.");
    }

    /**
     * Test for creating a manager account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateManager(){
        //Arrange
        when(managerRepository.findAll()).thenReturn(List.of()); //mock there being no manager in the system
        //Act
        Manager manager = accountManagementService.createManager();

        //Assert
        assertNotNull(manager);
        assertEquals("manager", manager.getUsername());
        assertEquals("manager", manager.getPassword());
        verify(managerRepository, times(1)).save(manager);
    }

    /**
     * Test for creating a manager account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateSecondManager(){
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.createManager());

        //Assert
        assertEquals(exception.getMessage(),"There can only be one manager in the system");
    }


    /**
     * @author Tarek namani
     *
     * Test for fetching the set of all employees in the system
     */
    @Test
    public void testGetSetOfEmployees(){

        //Act
        Set<Employee> loadedEmployees = accountManagementService.getSetOfEmployees();

        //Assert
        assertNotNull(loadedEmployees);
        assertEquals(employees,loadedEmployees);
        verify(employeeRepository, times(1)).findAll();
    }

    /**
     * @author Tarek namani
     *
     * Test for fetching the set of all employees in the system when there are no employees
     */
    @Test
    public void testGetSetOfNonExistentEmployees(){
        //Arrange
        when(employeeRepository.findAll()).thenReturn(List.of()); //mock there being no employees in the system

        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountManagementService.getSetOfEmployees());

        //Assert

        assertEquals("There are no employees in the system",exception.getMessage());

    }


    /**
     * @author Tarek namani
     *
     * Test for fetching the set of all customers in the system
     */
    @Test
    public void testGetSetOfCustomers(){

        //Act
        Set<Customer> loadedCustomers = accountManagementService.getSetOfCustomers();

        //Assert
        assertNotNull(loadedCustomers);
        assertEquals(customers,loadedCustomers);
        verify(customerRepository, times(1)).findAll();
    }

    /**
     * @author Tarek namani
     *
     * Test for fetching the set of all customers in the system when there are no customers
     */
    @Test
    public void testGetSetOfNonExistentCustomers(){
        //Arrange
        when(customerRepository.findAll()).thenReturn(null); //mock there being no customers in the system

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountManagementService.getSetOfCustomers());
        //Assert

        assertEquals("There are no customers in the system",exception.getMessage());

    }

    /**
     * @author Tarek namani
     *
     * Test for fetching an employee by username
     */
    @Test
    public void testGetEmployeeByUsername(){
        //Act
        Employee loadedEmployee = accountManagementService.getEmployeeByUsername("employee1");

        assertNotNull(loadedEmployee);
        assertEquals(employee1,loadedEmployee);
        verify(employeeRepository, times(1)).findByUsername("employee1");
    }

    /**
     * @author Tarek namani
     *
     * Test for fetching an employee with an invalid username
     */
    @Test
    public void testGetEmployeeByInvalidUsername(){
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->accountManagementService.getEmployeeByUsername(" "));

        //Assert
        assertEquals(exception.getMessage(),"Username cannot be empty, null, or contain spaces.");

    }

    /**
     * @author Tarek namani
     *
     * Test for fetching an employee that does not exist
     */
    @Test
    public void testGetNonExistentEmployee(){
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->accountManagementService.getEmployeeByUsername("uniqueUsername"));

        //Assert
        assertEquals(exception.getMessage(),"Employee with this username does not exist.");

    }

    /**
     * @author Tarek namani
     *
     * Test for fetching the system's manager
     */
    @Test
    public void testGetManager(){

        //Act
        Manager loadedManager = accountManagementService.getManager();

        //Assert
        assertNotNull(loadedManager);
        assertEquals(manager,loadedManager);
        verify(managerRepository, times(1)).findAll();
    }

    /**
     * @author Tarek namani
     *
     * Test for fetching the system's manager when there is no manager
     */
    @Test
    public void testGetNonExistentManager(){
        //Arrange
        when(managerRepository.findAll()).thenReturn(null); //mock there being no manager in the system

        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->accountManagementService.getManager());

        //Assert
        assertEquals("Manager does not exist",exception.getMessage());

    }


    @Test
    /**
     * @author Tarek namani
     *
     * Test for fetching a customer with their email
     */
    public void testGetCustomerByEmail(){
        //Act
        Customer loadedCustomer = accountManagementService.getCustomerByEmail("customer1@email.com");

        assertNotNull(loadedCustomer);
        assertEquals(customer1,loadedCustomer);
        verify(customerRepository, times(1)).findByEmail("customer1@email.com");
    }

    /**
     * @author Tarek namani
     *
     * Test for fetching a customer with an invalid email
     */
    @Test
    public void testGetCustomerByInvalidEmail(){
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->accountManagementService.getCustomerByEmail(" "));

        //Assert
        assertEquals(exception.getMessage(),"Email cannot be empty, null, or contain spaces.");

    }

    /**
     * @author Tarek namani
     *
     * Test for fetching a customer that does not exist
     */
    @Test
    public void testGetNonExistentCEmail(){
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->accountManagementService.getCustomerByEmail("uniqueEmail"));

        //Assert
        assertEquals(exception.getMessage(),"Customer with this email does not exist.");

    }

    /**
     * Test for deactivating employee, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testDeactivateEmployee(){
        // Act
        accountManagementService.setEmployeeStatus(employee1.getId(),false);
        // Assert
        assertFalse(employee1.isActive());
        verify(employeeRepository, times(1)).findById(employee1.getId());
    }

    /**
     * Test for activating employee, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testActivateEmployee(){
        //Arrange
        employee1.setActive(false);

        // Act
        accountManagementService.setEmployeeStatus(employee1.getId(),true);
        // Assert
        assertTrue(employee1.isActive());
        verify(employeeRepository, times(1)).findById(employee1.getId());
    }

    /**
     * Test for activating employee, fail
     *
     * @author Ana Gordon
     */
    @Test
    public void testActivateEmployeeInvalid(){
        // Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountManagementService.setEmployeeStatus(-1, false));

        assertEquals(exception.getMessage(),"Employee does not exist");
    }

    /**
     * Test for logging in to a customer account
     *  @author Tarek Namani
     */
    @Test
    public void testCustomerLogin() {
        //Arrange
        String email = "customer1@email.com";
        String password = "password1";


        //Act
        Customer loadedCustomer = accountManagementService.customerLogin(email, password);

        //Assert
        assertNotNull(loadedCustomer);
        assertEquals(customer1,loadedCustomer);
        verify(customerRepository, times(1)).findByEmail(email);

    }

    /**
     * Test for logging in to a customer account with the wrong password
     * @author Tarek Namani
     */
    @Test
    public void testCustomerWrongPassword() {
        //Arrange
        String email = "customer1@email.com";
        String password = "NotPassword";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.customerLogin(email, password));

        //Assert
        assertEquals(exception.getMessage(),"Wrong password!");

    }

    /**
     * Test for logging in to a customer account with an invalid password
     * @author Tarek Namani
     */
    @Test
    public void testCustomerInvalidPassword() {
        //Arrange
        String email = "customer1@email.com";
        String password = " ";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.customerLogin(email, password));

        //Assert
        assertEquals(exception.getMessage(),"Password cannot be empty, null or contain spaces.");

    }

    /**
     * Test for logging in to a customer account with an invalid email
     * @author Tarek Namani
     */
    @Test
    public void testCustomerInvalidEmail() {
        //Arrange
        String email = "customer1@email. com";
        String password = "validPassword";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.customerLogin(email, password));

        //Assert
        assertEquals(exception.getMessage(),"Email cannot be empty, null or contain spaces.");

    }

    /**
     * Test for logging in to a customer account that does not exist
     * @author Tarek Namani
     */
    @Test
    public void testNonExistentCustomerLogin() {
        //Arrange
        String email = "uniqueEmail";
        String password = "validPassword";

        //Act
        EntityNotFoundException  exception = assertThrows(EntityNotFoundException .class ,()->accountManagementService.customerLogin(email, password));

        //Assert
        assertEquals(exception.getMessage(),"Customer does not exist");

    }





    /**
     * Test for logging in to an employee account
     *  @author Tarek Namani
     */
    @Test
    public void testEmployeeLogin() {
        //Arrange
        String username = "employee1";
        String password = "password1";


        //Act
        Employee loadedEmployee = accountManagementService.employeeLogin(username, password);

        //Assert
        assertNotNull(loadedEmployee);
        assertEquals(employee1,loadedEmployee);
        verify(employeeRepository, times(1)).findByUsername(employee1.getUsername());

    }


    /**
     * Test for logging in to an employee account with the wrong password
     * @author Tarek Namani
     */
    @Test
    public void testEmployeeLoginWrongPassword() {
        //Arrange
        String username = "employee1";
        String password = "NotPassword";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.employeeLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Wrong password!");

    }

    /**
     * Test for logging in to  an employee account with an invalid password
     * @author Tarek Namani
     */
    @Test
    public void testEmployeeLoginInvalidPassword() {
        //Arrange
        String username = "employee1";
        String password = "NotPass word";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.employeeLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Password cannot be empty, null or contain spaces.");

    }

    /**
     * Test for logging in to an employee account with an invalid username
     * @author Tarek Namani
     */
    @Test
    public void testEmployeeLoginInvalidEmail() {
        //Arrange
        String username = "employee 1";
        String password = "NotPassword";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.employeeLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Username cannot be empty, null or contain spaces.");

    }

    /**
     * Test for logging in to  an employee account that does not exist
     * @author Tarek Namani
     */
    @Test
    public void testNonExistentEmployeeLogin() {
        //Arrange
        String username = "uniqueUsername";
        String password = "NotPassword";


        //Act
        EntityNotFoundException  exception = assertThrows(EntityNotFoundException .class ,()->accountManagementService.employeeLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Employee does not exist");

    }

    /**
     * Test for logging in to  an employee account that is deactivated
     * @author Tarek Namani
     */
    @Test
    public void testLoginInactiveEmployee() {
        //Arrange
        employee1.setActive(false); //deactivate the employee
        String username = "employee1";
        String password = "password1";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.employeeLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Account is deactivated!");

    }

    /**
     * Test for logging in to a manager account
     *  @author Tarek Namani
     */
    @Test
    public void testManagerLogin() {
        //Arrange
        String username = "manager";
        String password = "manager";


        //Act
        Manager loadedManager = accountManagementService.managerLogin(username, password);

        //Assert
        assertNotNull(loadedManager);
        assertEquals(manager,loadedManager);
        verify(managerRepository, times(1)).findByUsername(username);

    }


    /**
     * Test for logging in to an employee account with the wrong password
     * @author Tarek Namani
     */
    @Test
    public void testManagerLoginWrongPassword() {
        //Arrange
        String username = "manager";
        String password = "NotPassword";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.managerLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Wrong password!");

    }

    /**
     * Test for logging in to a manager account with an invalid password
     * @author Tarek Namani
     */
    @Test
    public void testManagerLoginInvalidPassword() {
        //Arrange
        String username = "manager";
        String password = "NotPass word";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.managerLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Password cannot be empty, null or contain spaces.");

    }

    /**
     * Test for logging in to a manager account with an invalid username
     * @author Tarek Namani
     */
    @Test
    public void testManagerLoginInvalidUsername() {
        //Arrange
        String username = "employee 1";
        String password = "NotPassword";


        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,()->accountManagementService.managerLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Username cannot be empty, null or contain spaces.");

    }

    /**
     * Test for logging in to  an employee account that does not exist
     * @author Tarek Namani
     */
    @Test
    public void testNonExistentManagerLogin() {
        //Arrange

        String username = "uniqueUsername";
        String password = "NotPassword";


        //Act
        EntityNotFoundException  exception = assertThrows(EntityNotFoundException .class ,()->accountManagementService.managerLogin(username, password));

        //Assert
        assertEquals(exception.getMessage(),"Manager does not exist");

    }

    /**
     * Update customer password, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerPassword(){
        //Arrange
        String email = customer1.getEmail();
        String oldPassword = customer1.getPassword();
        String newPassowrd = "newValidPassword";


        // Act
       Customer updatedCustomer = accountManagementService.updateCustomerPassword(oldPassword, newPassowrd, email);

       //Assert
        assertNotNull(updatedCustomer);
        assertEquals(newPassowrd, updatedCustomer.getPassword());
        verify(customerRepository, times(1)).findByEmail(updatedCustomer.getEmail());
        verify(customerRepository, times(1)).save(updatedCustomer);
    }

    /**
     * Update customer with an invalid old password
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateCustomerPasswordInvalidOldPassword(){
        //Arrange
        String email = customer1.getEmail();
        String oldPassword = " ";
        String newPassowrd = "newValidPassword";


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.updateCustomerPassword(oldPassword, newPassowrd, email));

        //Assert
        assertEquals("Old password cannot be empty, null or contain spaces.", exception.getMessage());
    }

    /**
     * Update customer with an invalid new password
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateCustomerPasswordInvalidNewPassword(){
        //Arrange
        String email = customer1.getEmail();
        String oldPassword = "valid";
        String newPassowrd = "new ValidPassword";


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.updateCustomerPassword(oldPassword, newPassowrd, email));

        //Assert
        assertEquals("New password cannot be empty, null or contain spaces.", exception.getMessage());
    }

    /**
     * Update customer with an invalid new password
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateCustomerPasswordInvalidEmail(){
        //Arrange
        String email = null;
        String oldPassword = "valid";
        String newPassowrd = "newValidPassword";


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.updateCustomerPassword(oldPassword, newPassowrd, email));

        //Assert
        assertEquals("Email cannot be empty, null or contain spaces.", exception.getMessage());
    }


    /**
     * Update customer with the wrong old password
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateCustomerPasswordWrongPassword(){
        //Arrange
        String email = customer1.getEmail();
        String oldPassword = "wrong";
        String newPassowrd = "newValidPassword";


        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.updateCustomerPassword(oldPassword, newPassowrd, email));

        //Assert
        assertEquals("Incorrect password.", exception.getMessage());
    }

    /**
     * Attempt to update the password of a customer that does not exist
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateNonExistentCustomerPassword(){
        //Arrange
        String email = "UniqueEmail";
        String oldPassword = "wrong";
        String newPassowrd = "newValidPassword";


        // Act
        EntityNotFoundException  exception = assertThrows(EntityNotFoundException .class, ()->accountManagementService.updateCustomerPassword(oldPassword, newPassowrd, email));

        //Assert
        assertEquals("Customer does not exist", exception.getMessage());
    }

    /**
     * Test for updating a customer account username, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerUsername(){
        //Arrange
        String email = customer1.getEmail();
        String newUsername = "UniqueUsername";
        String oldUsername = customer1.getUsername();

        //Act
        Customer updatedCustomer = accountManagementService.updateCustomerUsername(newUsername, email);

        //Assert
        assertNotNull(updatedCustomer);
        assertEquals(newUsername, updatedCustomer.getUsername());
        assertNotEquals(oldUsername, updatedCustomer.getUsername()); //ensure that username was changed
        verify(customerRepository, times(1)).findByEmail(updatedCustomer.getEmail());
        verify(customerRepository, times(1)).save(updatedCustomer);

    }

    /**
     * Attempt to update the username of a customer with an invalid email
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateCustomerUsernameInvalidEmail(){
        //Arrange
        String email = null;
        String newUsername = "UniqueUsername";

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.updateCustomerUsername(newUsername, email));


        //Assert
        assertEquals("Email cannot be empty, null or contain spaces.", exception.getMessage());
    }

    /**
     * Attempt to update the username of a customer with an invalid username
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateCustomerUsernameInvalidUsername(){
        //Arrange
        String email = "validEmail";
        String newUsername = "Uniq ueUsername";

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.updateCustomerUsername(newUsername, email));


        //Assert
        assertEquals("New username cannot be empty, null or contain spaces.", exception.getMessage());
    }


    /**
     * Attempt to update the username of a customer that does not exist
     *
     * @author Tarek Namani
     */
    @Test
    public void testUpdateNonExistentCustomerUsername(){
        //Arrange
        String email = "uniqueEmail";
        String newUsername = "validUsername";

        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountManagementService.updateCustomerUsername(newUsername, email));


        //Assert
        assertEquals("Customer does not exist", exception.getMessage());
    }

    // /**
    //  * Test for updating a customer account email, success
    //  *
    //  * @author Ana Gordon
    //  */
    // @Test
    // public void testUpdateCustomerEmail(){
    //     //Arrange
    //     String oldEmail = customer1.getEmail();
    //     String newEmail = "validEmail";

    //     //Act
    //     Customer updatedCustomer = accountManagementService.updateCustomerEmail(newEmail, oldEmail);

    //     //Assert
    //     assertNotNull(updatedCustomer);
    //     assertEquals(newEmail, updatedCustomer.getEmail());
    //     assertNotEquals(oldEmail, updatedCustomer.getEmail()); //ensure that email was changed
    //     verify(customerRepository, times(1)).findByEmail(updatedCustomer.getEmail());
    //     verify(customerRepository, times(1)).save(updatedCustomer);

    // }

    // /**
    //  * Attempt to update the email of a customer that does not exist
    //  *
    //  * @author Tarek Namani
    //  */
    // @Test
    // public void testUpdateNonExistentCustomerEmail(){
    //     //Arrange
    //     String oldEmail = "uniqueEmail";
    //     String newEmail = "validEmail";

    //     //Act
    //     EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountManagementService.updateCustomerEmail(newEmail, oldEmail));


    //     //Assert
    //     assertEquals("Customer does not exist", exception.getMessage());
    // }

    // /**
    //  * Attempt to update the email of a customer to one that is taken
    //  *
    //  * @author Tarek Namani
    //  */
    // @Test
    // public void testUpdateTakenCustomerEmail(){
    //     //Arrange
    //     String oldEmail = customer1.getEmail();
    //     String newEmail = customer2.getEmail();

    //     //Act
    //     IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.updateCustomerEmail(newEmail, oldEmail));


    //     //Assert
    //     assertEquals("Customer already exists with that email.", exception.getMessage());
    // }
    // /**
    //  * Attempt to update the email of a customer to one that is invalid
    //  *
    //  * @author Tarek Namani
    //  */
    // @Test
    // public void testUpdateCustomerEmailWithInvalidNewEmail(){
    //     //Arrange
    //     String oldEmail = "uniqueEmail";
    //     String newEmail = "";

    //     //Act
    //     IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.updateCustomerEmail(newEmail, oldEmail));


    //     //Assert
    //     assertEquals("New email cannot be empty, null or contain spaces.", exception.getMessage());
    // }


    // /**
    //  * Attempt to update the invalid email of a customer
    //  *
    //  * @author Tarek Namani
    //  */
    // @Test
    // public void testUpdateCustomerEmailWithInvalidOldEmail(){
    //     //Arrange
    //     String oldEmail = null;
    //     String newEmail = "valid";

    //     //Act
    //     IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.updateCustomerEmail(newEmail, oldEmail));


    //     //Assert
    //     assertEquals("Old email cannot be empty, null or contain spaces.", exception.getMessage());
    // }

    /**
     * Test for updating an employee account username, success
     *
     * @author Ana Gordon
     */
    @Test
    public void testUpdateEmployeeUsername(){
        //Arrange
        String oldUsername = employee1.getUsername();
        String newUsername = "uniqueUsername";

        //Act
        Employee updatedEmployee = accountManagementService.updateEmployeeUsername(newUsername,oldUsername);

        //Assert
        assertNotNull(updatedEmployee);
        assertEquals(newUsername, updatedEmployee.getUsername());
        assertNotEquals(oldUsername, updatedEmployee.getUsername()); //ensure that username was changed
        verify(employeeRepository, times(1)).findByUsername(updatedEmployee.getUsername());
        verify(employeeRepository, times(1)).save(updatedEmployee);

    }

    /**
     * Test for updating an employee account with an invalid username, fail
     *
     * @author Ana Gordon
     */
    @Test
    public void testUpdateEmployeeInvalidUsername(){
        //Arrange
        String oldUsername = employee1.getUsername();
        String newUsername = "unique Username";

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->accountManagementService.updateEmployeeUsername(newUsername,oldUsername));

        //Assert
        assertEquals("New username cannot be empty, null or contain spaces.", exception.getMessage());

    }

    /**
     * Test for updating an invalid employee account's username with an invalid username, fail
     *
     * @author Ana Gordon
     */
    @Test
    public void testUpdateEmployeeInvalidUsername2(){
        //Arrange
        String oldUsername = null;
        String newUsername = "uniqueUsername";

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->accountManagementService.updateEmployeeUsername(newUsername,oldUsername));

        //Assert
        assertEquals("Old username cannot be empty, null or contain spaces.", exception.getMessage());

    }

    /**
     * Test for updating an employee account username that does not exist, fail
     *
     * @author Ana Gordon
     */
    @Test
    public void testUpdateNonExistentEmployeeUsername(){
        //Arrange
        String oldUsername = "uniqueUsername";
        String newUsername = "somethingElse";

        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->accountManagementService.updateEmployeeUsername(newUsername,oldUsername));

        //Assert
        assertEquals("Employee does not exist", exception.getMessage());

    }

    /**
     * Test for updating an employee account username that is already taken, fail
     *
     * @author Ana Gordon
     */
    @Test
    public void testUpdateTakenEmployeeUsername(){
        //Arrange
        String oldUsername = employee1.getUsername();
        String newUsername = employee2.getUsername();

        //Act
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->accountManagementService.updateEmployeeUsername(newUsername,oldUsername));

        //Assert
        assertEquals("Username is already in use by another employee", exception.getMessage());

    }

    /**
     * Test for updating the phone number of a customer
     * @author Tarek Namani
     *
     */
    @Test
    public void testUpdateCustomerPhoneNumber() {
        //Arrange
        String newPhoneNumber = "123123123123";
        String oldPhoneNumber = customer1.getPhoneNumber();
        String email = customer1.getEmail();

        //Act
        Customer updatedCustomer = accountManagementService.updateCustomerPhoneNumber(newPhoneNumber, email);

        //Assert
        assertNotNull(updatedCustomer);
        assertEquals(newPhoneNumber, updatedCustomer.getPhoneNumber());
        assertNotEquals(oldPhoneNumber, updatedCustomer.getPhoneNumber());
        verify(customerRepository, times(1)).findByEmail(updatedCustomer.getEmail());
        verify(customerRepository, times(1)).save(updatedCustomer);
    }

    /**
     * Test for updating the phone number of a customer with an invalid phone number
     * @author Tarek Namani
     *
     */
    @Test
    public void testUpdateCustomerPhoneNumberWithInvaldiPhoneNumber() {
        //Arrange
        String newPhoneNumber = "12312312 3123";
        String email = customer1.getEmail();

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.updateCustomerPhoneNumber(newPhoneNumber, email));

        //Assert
        assertEquals("New phone number cannot be empty, null or contain spaces.", exception.getMessage());
    }

    /**
     * Test for updating the phone number of a customer with an invalid email
     * @author Tarek Namani
     *
     */
    @Test
    public void testUpdateCustomerPhoneNumberWithInvalidEmail() {
        //Arrange
        String newPhoneNumber = "123123123123";
        String email = null;

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->accountManagementService.updateCustomerPhoneNumber(newPhoneNumber, email));

        //Assert
        assertEquals("Email cannot be empty, null or contain spaces.", exception.getMessage());
    }

    /**
     * Test for updating the phone number of a customer with an invalid email
     * @author Tarek Namani
     *
     */
    @Test
    public void testUpdateNonExistantCustomerPhoneNumber() {
        //Arrange
        String newPhoneNumber = "123123123";
        String email = "uniqueEmail";

        //Act
        EntityNotFoundException  exception = assertThrows(EntityNotFoundException .class, ()->accountManagementService.updateCustomerPhoneNumber(newPhoneNumber, email));

        //Assert
        assertEquals("Customer does not exist", exception.getMessage());
    }

    @Test
    public void testAddGameToWishlist() {
        accountManagementService.addGameToWishlist(referenceCustomer.getId(), referenceGame.getId());

        assertTrue(referenceCustomer.containsGameInWishlist(referenceGame));
        verify(customerRepository, times(1)).save(referenceCustomer);
    }

    @Test
    public void testRemoveGameFromWishlist() {
        referenceCustomer.addGameToWishlist(referenceGame);

        accountManagementService.removeGameFromWishlist(referenceCustomer.getId(), referenceGame.getId());

        assertFalse(referenceCustomer.containsGameInWishlist(referenceGame));
        verify(customerRepository, times(1)).save(referenceCustomer);
    }

    @Test
    public void testViewWishlist() {
        referenceCustomer.addGameToWishlist(referenceGame);

        Set<Game> wishlist = accountManagementService.viewWishlist(referenceCustomer.getId());

        assertEquals(1, wishlist.size());
        assertTrue(wishlist.contains(referenceGame));
    }

    @Test
    public void testAddValidAddress() {
        //Arrange
        String street = "st catherine";
        String zipCode = "H3X X9P";
        String country = "Canada";
        String province = "Quebec";
        String city = "Montreal";
        int initialSize = customer1.getCopyAddresses().size();

        //Act
        Address createdAddress = accountManagementService.createAddress(street,city,province,zipCode,country,customer1.getEmail());

        //Assert
        assertNotNull(createdAddress);
        assertEquals(initialSize+1,customer1.getCopyAddresses().size());
        assertTrue(customer1.containsAddress(createdAddress));
        verify(customerRepository, times(1)).save(customer1);
        verify(addressRepository, times(1)).save(createdAddress);

    }

    @Test
    public void testAddInvalidAddress() {
        //Arrange
        String street = "";
        String zipCode = "H3X X9P";
        String country = "Canada";
        String province = "Quebec";
        String city = "Montreal";
        int initialSize = customer1.getCopyAddresses().size();

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> accountManagementService.createAddress(street,city,province,zipCode,country,customer1.getEmail()));

        //Assert
        assertEquals("Address contains null or empty strings", exception.getMessage());

    }

    @Test
    public void testAddInvalidAddress2() {
        //Arrange
        String street = "a street";
        String zipCode = "";
        String country = "Canada";
        String province = "Quebec";
        String city = "Montreal";
        int initialSize = customer1.getCopyAddresses().size();

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> accountManagementService.createAddress(street,city,province,zipCode,country,customer1.getEmail()));

        //Assert
        assertEquals("Address contains null or empty strings", exception.getMessage());

    }

    @Test
    public void testAddInvalidAddress3() {
        //Arrange
        String street = "a street";
        String zipCode = "H3X X9P";
        String country = null;
        String province = "Quebec";
        String city = "Montreal";
        int initialSize = customer1.getCopyAddresses().size();

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> accountManagementService.createAddress(street,city,province,zipCode,country,customer1.getEmail()));

        //Assert
        assertEquals("Address contains null or empty strings", exception.getMessage());

    }

    @Test
    public void testAddInvalidAddress4() {
        //Arrange
        String street = "a street";
        String zipCode = "H3X X9P";
        String country = "Canada";
        String province = null;
        String city = "Montreal";
        int initialSize = customer1.getCopyAddresses().size();

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> accountManagementService.createAddress(street,city,province,zipCode,country,customer1.getEmail()));

        //Assert
        assertEquals("Address contains null or empty strings", exception.getMessage());

    }

    @Test
    public void testAddInvalidAddress5() {
        //Arrange
        String street = "a street";
        String zipCode = "H3X X9P";
        String country = "Canada";
        String province = "Quebec";
        String city = "";
        int initialSize = customer1.getCopyAddresses().size();

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> accountManagementService.createAddress(street,city,province,zipCode,country,customer1.getEmail()));

        //Assert
        assertEquals("Address contains null or empty strings", exception.getMessage());

    }

    @Test
    public void testAddAddressForInexistentCustomer() {
        //Arrange
        String street = "a street";
        String zipCode = "H3X X9P";
        String country = "Canada";
        String province = "Quebec";
        String city = "a city";

        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, ()-> accountManagementService.createAddress(street,city,province,zipCode,country,"uniqueEmail"));

        //Assert
        assertEquals("Customer not found", exception.getMessage());

    }

    /**
     * Test finding an employee
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindEmployee() {
        //Act
        Employee loadedEmployee = accountManagementService.findEmployeeById(employee1.getId());

        //Assert
        assertNotNull(loadedEmployee);
        assertEquals(loadedEmployee.getUsername(), employee1.getUsername());
        assertEquals(loadedEmployee.getPassword(), employee1.getPassword());
        assertEquals(loadedEmployee.isActive(), employee1.isActive());
        assertEquals(loadedEmployee, employee1);
        verify(employeeRepository, times(1)).findById(employee1.getId());
    }

    /**
     * Test finding an employee with an invalid id
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindEmployeeInvalid() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.findEmployeeById(-1));

        //Assert
        assertEquals(exception.getMessage(), "No Employee found");
        verify(employeeRepository, times(1)).findById(-1);
    }
}

