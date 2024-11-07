package ca.mcgill.ecse321.gameshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import ca.mcgill.ecse321.gameshop.model.Account;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Manager;
import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.DAO.AccountRepository;
import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.ManagerRepository;
import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;

/**
 * Service class for the Account entity
 * 
 * @author Ana Gordon, 
 */
public class AccountManagementService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ManagerRepository managerRepository;

    /**
     * Create a new account
     * 
     * @param username
     * @param password
     * @return Account
     * 
     * @author Ana Gordon
     */
    @Transactional
    public Account createAccount(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            throw new IllegalArgumentException("Username cannot be empty or null.");
        }
        if (password == null || password.trim().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty or null.");
        }
        // if (accountRepository.findByUsername(username) != null) {
        //     throw new IllegalArgumentException("Email already in use.");
        // }
        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot contain spaces.");
        }
        if (password.contains(" ")) {
            throw new IllegalArgumentException("Password cannot contain spaces.");
        }
        if (accountRepository.findbyUsername(username) != null) {
            throw new IllegalArgumentException("Username already in use.");
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        accountRepository.save(account);
        return account;
    }

    /**
     * Create a customer from username
     * 
     * @param username
     * @param password
     * @param email
     * @param phoneNumber
     * @return Customer
     * 
     * @author Ana Gordon
     */
    @Transactional
    public Customer createCustomer(String email, String password, String username, String phoneNumber) {
        Account account = accountRepository.findByEmail(email);

        if (email == null || email.trim().length() == 0 || email.contains(" ")) {
            throw new IllegalArgumentException("Email cannot be empty, null, or contain spaces.");
        }
        if (accountRepository.findByEmail(email) == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        if (phoneNumber == null || phoneNumber.trim().length() == 0 || phoneNumber.contains(" ")) {
            throw new IllegalArgumentException("Phone number cannot be empty, null, or contain spaces.");
        }

        Customer customer = new Customer(username, password, email, phoneNumber);
        customer.setAccount(account);
        customerRepository.save(customer);
        return customer;
    }

    /**
     * Create an employee from username
     * 
     * @param username
     * @param password
     * @param is_active
     * @return Employee
     * 
     * @author Ana Gordon
     */
    @Transactional
    public Employee createEmployee(String username, String password, Boolean is_active) {
        Account account = accountRepository.findByUsername(username);

        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null, or contain spaces.");
        }
        if (accountRepository.findByUsername(username) == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        
        Employee employee = employeeRepository.findByAccount(account);
        if (employee != null) {
            throw new IllegalArgumentException("Employee account already exists.");
        }
        employee = new Employee(username, password, is_active);
        employee.setAccount(account);
        employeeRepository.save(employee);
        return employee;
    }

    /**
     * Create a manager from username
     * 
     * @param username
     * @param password
     * @return Manager
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Manager createManager() {
        Account account = accountRepository.findByUsername("manager");

        if (account != null) {
            return managerRepository.findByAccount(account);
        }
        //manager account has all permissions of an employee and a customer
        account = new Account("manager", "manager");
        accountRepository.save(account);

        Customer customer = new Customer(account);
        customerRepository.save(customer);

        Employee employee = new Employee(account);
        employeeRepository.save(employee);

        Manager manager = new Manager(account);
        managerRepository.save(manager);

        return manager;
    }

    /**
     * Deactivate an employee account by username
     * 
     * @param username
     * @return boolean
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public boolean deactivateEmployee(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        Employee employee = employeeRepository.findByAccount(account);
        if (employee == null) {
            throw new IllegalArgumentException("Employee does not exist.");
        }
        employee.setActive(false);
        employeeRepository.save(employee);
        return true;
    }

    /**
     * Login to an account
     * 
     * @param username
     * @param password
     * @return Account
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Account login(String username, String password) {
        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }
        if (password == null || password.trim().length() == 0 || password.contains(" ")) {
            throw new IllegalArgumentException("Password cannot be empty, null or contain spaces.");
        }

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        if (!account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        //check if account is an employee
        Employee employee = employeeRepository.findByAccount(account);
        if (employee != null) {
            if (!employee.getActive()) {
                throw new IllegalArgumentException("Employee account is deactivated.");
            }
        }

        //check if account is the manager
        Manager manager = managerRepository.findByAccount(account);
        if (manager == null) {
            throw new IllegalArgumentException("Manager account does not exist.");
        }
        return account;
    }

    /**
     * Logout of an account
     * 
     * @param username
     * @return boolean
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public boolean logout(String username) {
        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        return true;
    }

    /**
     * Update account password
     * 
     * @param oldPassword
     * @param newPassword
     * @param username
     * @return Account
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Account updatePassword(String oldPassword, String newPassword, String username) {
        if (oldPassword == null || oldPassword.trim().length() == 0 || oldPassword.contains(" ")) {
            throw new IllegalArgumentException("Old password cannot be empty, null or contain spaces.");
        }
        if (newPassword == null || newPassword.trim().length() == 0 || newPassword.contains(" ")) {
            throw new IllegalArgumentException("New password cannot be empty, null or contain spaces.");
        }
        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        if (!account.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        Employee employee = employeeRepository.findByAccount(account);
        if (employee != null) {
            throw new IllegalArgumentException("Employee accounts cannot change password.");
        }
        account.setPassword(newPassword);
        accountRepository.save(account);
        return account;
    }

    /**
     * Update account username
     * 
     * @param newUsername
     * @param oldusername
     * @return Account
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Account updateUsername(String newUsername, String oldUsername) {
        if (newUsername == null || newUsername.trim().length() == 0 || newUsername.contains(" ")) {
            throw new IllegalArgumentException("New username cannot be empty, null or contain spaces.");
        }
        if (oldUsername == null || oldUsername.trim().length() == 0 || oldUsername.contains(" ")) {
            throw new IllegalArgumentException("Old username cannot be empty, null or contain spaces.");
        }

        Account account = accountRepository.findByUsername(oldUsername);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        if (accountRepository.findByUsername(newUsername) != null) {
            throw new IllegalArgumentException("Username already in use.");
        }

        Employee employee = employeeRepository.findByAccount(account);
        if (employee != null) {
            throw new IllegalArgumentException("Employee accounts cannot change username.");
        }
        account.setUsername(newUsername);
        accountRepository.save(account);
        return account;
    }

    /**
     * Update customer email
     * 
     * @param newEmail
     * @param username
     * @return Customer
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Customer updateEmail(String newEmail, String username) {
        if (newEmail == null || newEmail.trim().length() == 0 || newEmail.contains(" ")) {
            throw new IllegalArgumentException("New email cannot be empty, null or contain spaces.");
        }
        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }

        Customer customer = customerRepository.findByAccount(account);
        if (customer == null) {
            throw new IllegalArgumentException("Customer account does not exist.");
        }
        customer.setEmail(newEmail);
        customerRepository.save(customer);
        return customer;
    }

    /**
     * Update customer phone number
     * 
     * @param newPhoneNumber
     * @param username
     * @return Customer
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Customer updatePhoneNumber(String newPhoneNumber, String username) {
        if (newPhoneNumber == null || newPhoneNumber.trim().length() == 0 || newPhoneNumber.contains(" ")) {
            throw new IllegalArgumentException("New phone number cannot be empty, null or contain spaces.");
        }
        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist.");
        }

        Customer customer = customerRepository.findByAccount(account);
        if (customer == null) {
            throw new IllegalArgumentException("Customer account does not exist.");
        }
        customer.setPhoneNumber(newPhoneNumber);
        customerRepository.save(customer);
        return customer;
    }

    
}
