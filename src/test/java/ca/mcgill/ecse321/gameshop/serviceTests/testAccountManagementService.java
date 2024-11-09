package test.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

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

import org.springframework.beans.factory.annotation.Autowired;
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

    @SuppressWarnings("null")
    @BeforeEach
    public void setUp(){
        //initialize all fields
        account1 = new Account("account1", "password1");
        // account2 = new Account("account2", "password2");
        customer1 = new Customer("customer1", "password1", "customer1@email.com", "0123456789");
        employee1 = new Employee("employee1", "password1", true);
        employee2 = new Employee("employee2", "password2", false);
        manager = new Manager("manager", "manager");

        when(accountRepository.save(any(Account.class))).thenReturn(account1);
        when(accountRepository.findByUsername(anyString())).thenReturn(account1);
        when(accountRepository.findByAccountID(anyInt())).thenReturn(account1);
        when(accountRepository.findByUsername("invalidUsername")).thenReturn(null);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer1);
        when(customerRepository.findByCustomerID(0)).thenReturn(customer1);
        when(customerRepository.findByCustomerID(1)).thenReturn(customer2);
        when(customerRepository.findByCustomerID(-1)).thenReturn(null);
        when(customerRepository.findByEmail(customer1.getEmail())).thenReturn(Optional.of(customer1));
        when(customerRepository.findByEmail(customer2.getEmail())).thenReturn(Optional.of(customer2));
        when(customerRepository.findByEmail("invalidEmail")).thenReturn(Optional.empty());

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);
        when(employeeRepository.findByEmployeeID(0)).thenReturn(employee1);
        when(employeeRepository.findByEmployeeID(1)).thenReturn(employee2);
        when(employeeRepository.findByEmployeeID(-1)).thenReturn(null);

        when(managerRepository.save(any(Manager.class))).thenReturn(manager);
        when(managerRepository.findByManagerID(anyInt())).thenReturn(manager);
    }

    /**
     * Test for creating an account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateAccount(){
        // Act
        Account account = null;
        try {
            account = accountManagementService.createAccount("account1", "password1");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("account1", account.getUsername());
        assertEquals("password1", account.getPassword());
    }

    /**
     * Test for creating an account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateAccountUsernameNull(){
        // Act
        Account account = null;
        String error = null;
        try {
            account = accountManagementService.createAccount(null, "password1");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Username cannot be null", error);
    }

    /**
     * Test for creating an account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateAccountPasswordNull(){
        // Act
        Account account = null;
        String error = null;
        try {
            account = accountManagementService.createAccount("account1", null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Password cannot be null", error);
    }

    /**
     * Test for creating a customer account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateCustomerAccount(){
        // Act
        Customer customer = null;
        try {
            customer = accountManagementService.createCustomerAccount("customer1", "password1", "email", "phone");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(customer);
        assertEquals("customer1", customer.getUsername());
        assertEquals("password1", customer.getPassword());
        assertEquals("email", customer.getEmail());
        assertEquals("phone", customer.getPhoneNumber());
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateCustomerAccountEmailNull(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomerAccount("customer1", "password1", null, "phone");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Email cannot be null", error);
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateCustomerAccountPhoneNull(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomerAccount("customer1", "password1", "email", null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Phone number cannot be null", error);
    }

    /**
     * Tets for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateCustomerAccountEmailInvalid(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomerAccount("customer1", "password1", "invalidEmail", "phone");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Invalid email", error);
    }

    /** 
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateCustomerAccountUsernameNull(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomerAccount(null, "password1", "email", "phone");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Username cannot be null", error);
    }

    /**
     * Test for creating a customer account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateCustomerAccountPasswordNull(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.createCustomerAccount("customer1", null, "email", "phone");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Password cannot be null", error);
    }

    /**
     * Test for creating an employee account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateEmployeeAccount(){
        // Act
        Employee employee = null;
        try {
            employee = accountManagementService.createEmployeeAccount("employee1", "password1", true);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(employee);
        assertEquals("employee1", employee.getUsername());
        assertEquals("password1", employee.getPassword());
        assertTrue(employee.getIsManager());
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateEmployeeAccountNotActive(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployeeAccount("employee1", "password1", false);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNotNull(employee);
        assertEquals("Employee must be active when created", error);
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateEmployeeAccountUsernameNull(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployeeAccount(null, "password1", true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Username cannot be null", error);
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateEmployeeAccountPasswordNull(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployeeAccount("employee1", null, true);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Password cannot be null", error);
    }

    /**
     * Test for creating an employee account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateEmployeeAccountIs_ActiveNull(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.createEmployeeAccount("employee1", "password1", null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Is_Active cannot be null", error);
    }

    /**
     * Test for creating a manager account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateManagerAccount(){
        // Act
        Manager manager = null;
        try {
            manager = accountManagementService.createManagerAccount("manager", "manager");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(manager);
        assertEquals("manager", manager.getUsername());
        assertEquals("manager", manager.getPassword());
    }

    /**
     * Test for creating a manager account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateManagerAccountUsernameNull(){
        // Act
        Manager manager = null;
        String error = null;
        try {
            manager = accountManagementService.createManagerAccount(null, "manager");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(manager);
        assertEquals("Username cannot be null", error);
    }

    /**
     * Test for creating a manager account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testCreateManagerAccountPasswordNull(){
        // Act
        Manager manager = null;
        String error = null;
        try {
            manager = accountManagementService.createManagerAccount("manager", null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(manager);
        assertEquals("Password cannot be null", error);
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
        try {
            account = accountManagementService.getAccountByUsername("account1");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("account1", account.getUsername());
        assertEquals("password1", account.getPassword());
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
    }

    /**
     * Test for getting an account by accountID, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testGetAccountByAccountID(){
        // Act
        Account account = null;
        try {
            account = accountManagementService.getAccountByAccountID(1);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("account1", account.getUsername());
        assertEquals("password1", account.getPassword());
    }

    /**
     * Test for getting an account by accountID, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testGetAccountByAccountIDInvalid(){
        // Act
        Account account = null;
        String error = null;
        try {
            account = accountManagementService.getAccountByAccountID(-1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Account not found", error);
    }

    /**
     * Test for deactivating employee, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testDeactivateEmployee(){
        // Act
        Employee employee = null;
        try {
            employee = accountManagementService.deactivateEmployee(0);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(employee);
        assertFalse(employee.getIsManager());
    }

    /**
     * Test for deactivating employee, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testDeactivateEmployeeInvalid(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.deactivateEmployee(-1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Employee not found", error);
    }

    /**
     * Test for activating employee, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testActivateEmployee(){
        // Act
        Employee employee = null;
        try {
            employee = accountManagementService.activateEmployee(1);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(employee);
        assertTrue(employee.getIsManager());
    }

    /**
     * Test for activating employee, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testActivateEmployeeInvalid(){
        // Act
        Employee employee = null;
        String error = null;
        try {
            employee = accountManagementService.activateEmployee(-1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(employee);
        assertEquals("Employee not found", error);
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
            account = accountManagementService.login("account1", "password1");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("account1", account.getUsername());
        assertEquals("password1", account.getPassword());
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
            account = accountManagementService.login("invalidUsername", "password1");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Account not found", error);
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
            account = accountManagementService.login("account1", "invalidPassword");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Invalid password", error);
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
            customer = accountManagementService.updateCustomerPassword(0, "newPassword");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(customer);
        assertEquals("newPassword", customer.getPassword());
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
            customer = accountManagementService.updateCustomerPassword(-1, "newPassword");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Customer not found", error);
    }

    /**
     * Test for loging out of an account, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testLogout(){
        // Act
        Account account = null;
        try {
            account = accountManagementService.logout("account1");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(account);
        assertEquals("account1", account.getUsername());
        assertEquals("password1", account.getPassword());
    }

    /**
     * Test for loging out of an account, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testLogoutInvalidUsername(){
        // Act
        Account account = null;
        String error = null;
        try {
            account = accountManagementService.logout("invalidUsername");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(account);
        assertEquals("Account not found", error);
    }

    /**
     * Test for updating an customer account username, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerUsername(){
        // Act
        Customer customer = null;
        try {
            customer = accountManagementService.updateCustomerUsername(0, "newUsername");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(customer);
        assertEquals("newUsername", customer.getUsername());
    }

    /**
     * Test for updating an customer account username, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerUsernameInvalid(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.updateCustomerUsername(-1, "newUsername");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Customer not found", error);
    }

    /**
     * Test for updating a customer account phone number, success
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerPhoneNumber(){
        // Act
        Customer customer = null;
        try {
            customer = accountManagementService.updateCustomerPhoneNumber(0, "newPhoneNumber");
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        // Assert
        assertNotNull(customer);
        assertEquals("newPhoneNumber", customer.getPhoneNumber());
    }

    /**
     * Test for updating a customer account phone number, fail
     * 
     * @author Ana Gordon
     */
    @Test
    public void testUpdateCustomerPhoneNumberInvalid(){
        // Act
        Customer customer = null;
        String error = null;
        try {
            customer = accountManagementService.updateCustomerPhoneNumber(-1, "newPhoneNumber");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        // Assert
        assertNull(customer);
        assertEquals("Customer not found", error);
    }


}
