package ca.mcgill.ecse321.gameshop.serviceClasses;

import jakarta.persistence.EntityNotFoundException;
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
@Service
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


        if (email == null || email.trim().length() == 0 || email.contains(" ")) {
            throw new IllegalArgumentException("Email cannot be empty, null, or contain spaces.");
        }

        if (phoneNumber == null || phoneNumber.trim().length() == 0 || phoneNumber.contains(" ")) {
            throw new IllegalArgumentException("Phone number cannot be empty, null, or contain spaces.");
        }

        if (customerRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Customer with this email already exists.");
        }

        Customer customer = new Customer(username, password, email, phoneNumber);
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

        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null, or contain spaces.");
        }

        if (!employeeRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Employee account already exists.");
        }
        Employee employee = new Employee(username, password, is_active);
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
    public Manager createManager(String username, String password) {
        Account account = accountRepository.findByUsername("manager");
        if (account != null) {
            throw new IllegalArgumentException("Account does not exist.");
        }

        Manager manager = new Manager("manager", "manager");
        managerRepository.save(manager);
        return manager;
    }

    /**
     * Activate an employee account by username
     * 
     * @param id
     * @return boolean
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public boolean activateEmployee(int id) {
        Employee employee = employeeRepository.findEmployeeById(id).orElseThrow(()-> new EntityNotFoundException("Employee does not exist"));
        if (employee.isActive()) {
            return false;
        }
        employee.setActive(true);
        employeeRepository.save(employee);
        return true;
    }

    /**
     * Deactivate an employee account by username
     * 
     * @param id
     * @return boolean
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public boolean deactivateEmployee(int id) {
        Employee employee = employeeRepository.findEmployeeById(id).orElseThrow(()-> new EntityNotFoundException("Employee does not exist"));
        if (!employee.isActive()) {
            return false;
        }
        employee.setActive(false);
        employeeRepository.save(employee);
        return true;
    }

    /**
     * Login to a customer account
     * 
     * @param email
     * @param password
     * @return Customer
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Customer customerLogin(String email, String password) {

        if (email == null || email.trim().isEmpty() || email.contains(" ")) {
            throw new IllegalArgumentException("Email cannot be empty, null or contain spaces.");
        }
        if (password == null || password.trim().isEmpty() || password.contains(" ")) {
            throw new IllegalArgumentException("Password cannot be empty, null or contain spaces.");
        }


        Customer customer = customerRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Customer does not exist"));
        if (!customer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password!");
        }

        return customer;
    }

    /**
     * Login to an employee account
     *
     * @param username
     * @param password
     * @return Employee
     *
     * @Author Tarek Namani
     */
    @Transactional
    public Employee employeeLogin(String username, String password) {

        if (username == null || username.trim().isEmpty() || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }
        if (password == null || password.trim().isEmpty() || password.contains(" ")) {
            throw new IllegalArgumentException("Password cannot be empty, null or contain spaces.");
        }


        Employee employee = employeeRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("Employee does not exist"));
        if (!employee.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password!");
        } else if (!employee.isActive()) {
            throw new IllegalArgumentException("Account is deactivated!");
        }

        return employee;
    }

    /**
     * Login to an employee account
     *
     * @param username
     * @param password
     * @return Manager
     *
     * @Author Tarek Namani
     */
    @Transactional
    public Manager managerLogin(String username, String password) {

        if (username == null || username.trim().isEmpty() || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }
        if (password == null || password.trim().isEmpty() || password.contains(" ")) {
            throw new IllegalArgumentException("Password cannot be empty, null or contain spaces.");
        }


        Manager manager = managerRepository.findManagerByUsername(username).orElseThrow(()-> new EntityNotFoundException("Manager does not exist"));
        if (!manager.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password!");
        }
        return manager;
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
     * @param email
     * @return Account
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Customer updateCustomerPassword(String oldPassword, String newPassword, String email) {
        if (oldPassword == null || oldPassword.trim().length() == 0 || oldPassword.contains(" ")) {
            throw new IllegalArgumentException("Old password cannot be empty, null or contain spaces.");
        }
        if (newPassword == null || newPassword.trim().length() == 0 || newPassword.contains(" ")) {
            throw new IllegalArgumentException("New password cannot be empty, null or contain spaces.");
        }
        if (email == null || email.trim().length() == 0 || email.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }

        Customer customer = customerRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Customer does not exist"));

        if (!customer.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        customer.setPassword(newPassword);
        accountRepository.save(customer);
        return customer;
    }

    /**
     * Update account username
     * @param newUsername
     * @param oldUsername
     * @oaram customerEmail
     * @return Account
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Customer updateCustomerUsername(String newUsername, String oldUsername, String customerEmail) {
        if (newUsername == null || newUsername.trim().length() == 0 || newUsername.contains(" ")) {
            throw new IllegalArgumentException("New username cannot be empty, null or contain spaces.");
        }
        if (oldUsername == null || oldUsername.trim().length() == 0 || oldUsername.contains(" ")) {
            throw new IllegalArgumentException("Old username cannot be empty, null or contain spaces.");
        }

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(()-> new EntityNotFoundException("Customer does not exist"));
        customer.setUsername(newUsername);
        customerRepository.save(customer);
        return customer;
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

        Customer customer = customerRepository.findByEmail(newEmail).orElseThrow(() -> new EntityNotFoundException("Customer does not exist"));

        customer.setEmail(newEmail);
        customerRepository.save(customer);
        return customer;
    }

    /**
     * Update customer phone number
     * 
     * @param newPhoneNumber
     * @param customerEmail
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public void updateCustomerPhoneNumber(String newPhoneNumber, String customerEmail) {
        if (newPhoneNumber == null || newPhoneNumber.trim().length() == 0 || newPhoneNumber.contains(" ")) {
            throw new IllegalArgumentException("New phone number cannot be empty, null or contain spaces.");
        }

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(() -> new EntityNotFoundException("Customer does not exist"));

        customer.setPhoneNumber(newPhoneNumber);
        customerRepository.save(customer);

    }

    /**
     * Get account by username
     * 
     * @param username
     * @return Account
     * 
     * @Author Ana Gordon
     */
    public Account getAccountByUsername(String username) {
        if (username == null || username.trim().length() == 0 || username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }
        Account account = accountRepository.findByUsername(username);
        return account;
    }

    /**
     * Get account by id
     * 
     * @param id
     * @return Account
     * 
     * @Author Ana Gordon
     */
    public Account getAccountByID(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative.");
        }
        Account account = accountRepository.findAccountById(id);
        return account;
    }


    
}
