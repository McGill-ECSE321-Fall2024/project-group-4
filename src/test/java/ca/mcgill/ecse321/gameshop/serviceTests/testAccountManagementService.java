package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * Unit test for AccountManagementService
 * 
 * @author Ana Gordon
 */

@SpringBootTest
public class testAccountManagementService {

    @InjectMocks
    private AccountManagementService accountManagementService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    // Initialize the account examples
    Account account1;
    Account account2;
    Customer customer1;
    Customer customer2;
    Employee employee1;
    Employee employee2;
    Manager manager;

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
    }


    @BeforeEach
    public void setUp(){
        //initialize all fields
        // account2 = new Account("account2", "password2");
        customer1 = new Customer("customer1", "password1", "customer1@email.com", "0123456789");
        employee1 = new Employee("employee1", "password1", true);
        employee2 = new Employee("employee2", "password2", false);
        manager = new Manager("manager", "manager");

        when(accountRepository.save(any(Account.class))).thenReturn(account1);
        when(accountRepository.findByUsername(anyString())).thenReturn(account1);
        when(accountRepository.findAccountById(anyInt())).thenReturn(account1);
        when(accountRepository.findByUsername("invalidUsername")).thenReturn(null);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer1);
        when(customerRepository.findCustomerById(0)).thenReturn(Optional.of(customer1));
        // when(customerRepository.findByCustomerID(1)).thenReturn(customer2);
        when(customerRepository.findCustomerById(-1)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(customer1.getEmail())).thenReturn(Optional.of(customer1));
        // when(customerRepository.findByEmail(customer2.getEmail())).thenReturn(Optional.of(customer2));
        when(customerRepository.findByEmail("invalidEmail")).thenReturn(Optional.empty());

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);
        when(employeeRepository.findEmployeeById(0)).thenReturn(Optional.of(employee1));
        when(employeeRepository.findEmployeeById(1)).thenReturn(Optional.of(employee2));
        when(employeeRepository.findEmployeeById(-1)).thenReturn(Optional.empty());

        when(managerRepository.save(any(Manager.class))).thenReturn(manager);
        when(managerRepository.findManagerById(anyInt())).thenReturn(manager);
    }

    /**
     * Test for creating a customer account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomer(){
        // Act
        Customer customer = null;
        try {
            customer = accountManagementService.createCustomer("customer1", "password1", "customer1@email.com", "0123456789");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(customer);
        assertEquals("customer1", customer.getUsername());
        assertEquals("password1", customer.getPassword());
        assertEquals("email", customer.getEmail());
        assertEquals("phone", customer.getPhoneNumber());
        verify(customerRepository, times(1)).findByEmail(customer1.getEmail());
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerEmailNull(){
        // Act
        Customer customer = null;
        String exception = null;
        try {
            customer = accountManagementService.createCustomer(null, customer1.getPassword(), customer1.getUsername(), customer1.getPhoneNumber());
        } catch (IllegalArgumentException e) {
            exception = e.getMessage();
        }
        // Assert

        assertEquals("Email is null!", exception);
        verify(customerRepository, times(0)).findByEmail(null);
    }

    /**
     * Tets for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerEmailInvalid(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomer("invalidEmail", customer1.getPassword(), customer1.getUsername(), customer1.getPhoneNumber());
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Invalid email", error);
        verify(customerRepository, times(1)).findByEmail("invalidEmail");
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerEmailAlreadyExists(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomer(customer1.getEmail(), customer1.getPassword(), customer1.getUsername(), customer1.getPhoneNumber());
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Email already exists", error);
        verify(customerRepository, times(1)).findByEmail(customer1.getEmail());
    }

    /** 
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerUsernameNull(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomer(null, "password1", "email", "phone");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Username cannot be null", error);
        verify(customerRepository, times(0)).findByEmail(null);
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateCustomerPasswordNull(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomer("customer1", null, "email", "phone");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Password cannot be null", error);
        verify(customerRepository, times(0)).findByEmail("customer1");
    }

    /**
     * Test for creating an employee account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateEmployee(){
        // Act
        Employee employee = null;
        try {
            employee = accountManagementService.createEmployee("employee1", "password1", true);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(employee);
        assertEquals("employee1", employee.getUsername());
        assertEquals("password1", employee.getPassword());
        verify(employeeRepository, times(1)).findByUsername("employee1");
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateEmployeeNotActive(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployee("employee1", "password1", false);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNotNull(employee);
        assertEquals("Employee must be active when created", error);
        verify(employeeRepository, times(1)).findByUsername("employee1");
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateEmployeeUsernameNull(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployee(null, "password1", true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Username cannot be null", error);
        verify(employeeRepository, times(0)).findByUsername(null);
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateEmployeePasswordNull(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployee("employee1", null, true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Password cannot be null", error);
        verify(employeeRepository, times(0)).findByUsername("employee1");
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateEmployeeIs_ActiveNull(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployee("employee1", "password1", null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Is_Active cannot be null", error);
        verify(employeeRepository, times(0)).findByUsername("employee1");
    }

    /**
     * Test for creating a manager account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateManager(){
        // Act
        Manager manager = null;
        try {
            manager = accountManagementService.createManager("manager", "manager");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(manager);
        assertEquals("manager", manager.getUsername());
        assertEquals("manager", manager.getPassword());
        verify(managerRepository, times(1)).findManagerById(anyInt());
    }

    /**
     * Test for creating a manager account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateManagerUsernameNull(){
        // Act
        Manager manager = null;
        String error = null;
        try {
            manager = accountManagementService.createManager(null, "manager");
            
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(manager);
        assertEquals("Username cannot be null", error);
        verify(managerRepository, times(0)).findManagerById(anyInt());
    }

    /**
     * Test for creating a manager account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testcreateManagerPasswordNull(){
        // Act
        Manager manager = null;
        String error = null;
        try {
            manager = accountManagementService.createManager("manager", null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(manager);
        assertEquals("Password cannot be null", error);
        verify(managerRepository, times(0)).findManagerById(anyInt());
    }

    /**
     * Test for getting an account by username, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testGetAccountByUsername(){
        // Act
        Account account = null;
        boolean result = false;
        try {
            account = accountManagementService.getAccountByUsername("account1");
            result = true;
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).findByUsername("account1");
    }

    /**
     * Test for getting an account by username, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testGetAccountByUsernameInvalid(){
        // Act
        Account account = null;
        String error = null;
        try {
            account = accountManagementService.getAccountByUsername("invalidUsername");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Account not found", error);
        verify(accountRepository, times(1)).findByUsername("invalidUsername");
    }

    /**
     * Test for getting an account by accountID, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testGetAccountByID(){
        // Act
        Account account = null;
        try {
            account = accountManagementService.getAccountByID(1);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("account1", account.getUsername());
        assertEquals("password1", account.getPassword());
        verify(accountRepository, times(1)).findAccountById(1);
    }

    /**
     * Test for getting an account by accountID, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testGetAccountByIDInvalid(){
        // Act
        Account account = null;
        String error = null;
        try {
            account = accountManagementService.getAccountByID(-1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Account not found", error);
        verify(accountRepository, times(0)).findAccountById(-1);
    }

    /**
     * Test for deactivating employee, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testDeactivateEmployee(){
        // Act
        Employee employee = new Employee("employee1", "password1", true);
        Boolean deactivated = null;
        try {
            deactivated = accountManagementService.deactivateEmployee(employee.getId());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(deactivated);
        verify(employeeRepository, times(1)).findByUsername("employee1");
    }

    /**
     * Test for deactivating employee, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testDeactivateEmployeeInvalid(){
        // Act
        String error = null;
        Employee employee = new Employee("employee1", "password1", true); //check this 
        Boolean deactivated = null;
        try {
            deactivated = accountManagementService.deactivateEmployee(employee.getId());
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(deactivated);
        assertEquals("Employee not found", error);
        verify(employeeRepository, times(0)).findByUsername("invalidUsername");
    }

    /**
     * Test for activating employee, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testActivateEmployee(){
        // Act
        Employee employee = new Employee("employee1", "password1", false);
        Boolean activated = null;
        try {
            activated = accountManagementService.activateEmployee(employee.getId());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(activated);
        verify(employeeRepository, times(1)).findByUsername("employee1");
    }

    /**
     * Test for activating employee, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testActivateEmployeeInvalid(){
        // Act
        String error = null;
        Boolean activated = null;

        try {
            activated = accountManagementService.activateEmployee(-1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(activated);
        assertEquals("Employee not found", error);
        verify(employeeRepository, times(0)).findByUsername("invalidUsername");
    }

    /** 
     * Test for loging into an account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testLogin(){
        // Act
        Account account = null;
        try {
            //account = accountManagementService.login("account1", "password1");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("account1", account.getUsername());
        assertEquals("password1", account.getPassword());
        verify(accountRepository, times(1)).findByUsername("account1");
    }

    /**
     * Test for loging into an account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testLoginInvalidUsername(){
        // Act
        Account account = null;
        String error = null;
        try {
            //account = accountManagementService.login("invalidUsername", "password1");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Account not found", error);
        verify(accountRepository, times(0)).findByUsername("invalidUsername");
    }

    /**
     * Test for loging into an account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testLoginInvalidPassword(){
        // Act
        Account account = null;
        String error = null;
        try {
          //  account = accountManagementService.login("account1", "invalidPassword");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Invalid password", error);
        verify(accountRepository, times(1)).findByUsername("account1");
    }

    /**
     * Update customer password, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerPassword(){
        // Act
        Customer customer = null;
        try {
            customer = accountManagementService.updateCustomerPassword("oldPassword", "newPassword", "customer1@email.com");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(customer);
        assertEquals("newPassword", customer.getPassword());
        verify(customerRepository, times(1)).findByEmail("customer1@email.com");
    }

    /**
     * Update customer password, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerPasswordInvalid(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.updateCustomerPassword("invalidPassword", "newPassword", "customer1@email.com");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Customer not found", error);
        verify(customerRepository, times(1)).findByEmail("customer1@email.com");
    }

    /**
     * Test for loging out of an account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testLogout(){
        // Act
        boolean result = false;
        try {
            result = accountManagementService.logout("account1");
        }
        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).findByUsername("account1");
    }

    /**
     * Test for loging out of an account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testLogoutInvalidUsername(){
        // Act
        boolean result = false;
        String error = null;
        try {
            result = accountManagementService.logout("invalidUsername");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertEquals("Account not found", error);
        verify(accountRepository, times(0)).findByUsername("invalidUsername");
    }

    /**
     * Test for updating an customer account username, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerUsername(){
        // Act
        Account account = null;
        try {
           // account = accountManagementService.updateUsername("newUsername", "oldUsername");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("newUsername", account.getUsername());
        verify(customerRepository, times(1)).findByEmail("oldUsername");
    }

    /**
     * Test for updating an customer account username, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerUsernameInvalid(){
        // Act
        Account account = null;
        String error = null;
        try {
         //   account = accountManagementService.updateUsername("newUsername", "oldUsername");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Customer not found", error);
        verify(customerRepository, times(1)).findByEmail("oldUsername");
    }

    /**
     * Test for updating a customer account phone number, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerPhoneNumber(){
        // Act
        accountManagementService.updateCustomerPhoneNumber("123456", customer1.getEmail());

        // Assert
        assertNotNull(customerRepository.findByEmail(customer1.getEmail()));
        assertEquals("123456", customer1.getPhoneNumber());
        verify(customerRepository, times(1)).findByEmail(customer1.getEmail());
    }

    /**
     * Test for updating a customer account phone number, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerPhoneNumberInvalid(){
        // Act
        EntityNotFoundException error = assertThrows(EntityNotFoundException.class, () -> accountManagementService.updateCustomerPhoneNumber("invalidPhoneNumber","invalidEmail" ));
        // Assert
        assertEquals("Customer not found", error);
        verify(customerRepository, times(0)).findByEmail("invalidEmail");
    }


}
