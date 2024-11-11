package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.CustomerRepository;
import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.DAO.ManagerRepository;
import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;

import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Manager;
import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.model.Game;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class for the Account entity
 * 
 * @author Ana Gordon, Tarek Namani, Clara Mickail
 */
@Service
public class AccountManagementService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ManagerRepository managerRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     *
     * private method to avoid code repetition, validates input strings
     *
     *
     * @param inputString
     * @return boolean
     *
     * @author Tarek Namani
     */

    private static boolean validateStringParameter(String inputString) {
        if (inputString == null || inputString.trim().isEmpty() || inputString.contains(" ")) {
            return false;
        }
        return true;
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


        if (!validateStringParameter(email)) {
            throw new IllegalArgumentException("Email cannot be empty, null, or contain spaces.");
        }

        if (!validateStringParameter(phoneNumber)) {
            throw new IllegalArgumentException("Phone number cannot be empty, null, or contain spaces.");
        }

        if (!validateStringParameter(username)) {
            throw new IllegalArgumentException("Username cannot be empty, null, or contain spaces.");
        }

        if (!validateStringParameter(password)) {
            throw new IllegalArgumentException("Password cannot be empty, null, or contain spaces.");
        }

        if (customerRepository.findByEmail(email).isPresent()) {
            throw new EntityExistsException("Customer with this email already exists.");
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
    public Employee createEmployee(String username, String password, boolean is_active) {

        if (!validateStringParameter(username)) {
            throw new IllegalArgumentException("Username cannot be empty, null, or contain spaces.");
        }

        if (!validateStringParameter(password)) {
            throw new IllegalArgumentException("Password cannot be empty, null, or contain spaces.");
        }

        if (!employeeRepository.findByUsername(username).isEmpty()) {
            throw new IllegalArgumentException("Employee already exists with that username.");
        }
        Employee employee = new Employee(username, password, is_active);
        employeeRepository.save(employee);
        return employee;
    }

    /**
     * Create a manager with default username and passwords
     * 
     *
     * @return Manager
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Manager createManager() {
        Iterable<Manager> managers = managerRepository.findAll();
        if (managers == null || !managers.iterator().hasNext()) {
            Manager manager = new Manager("manager", "manager");
            managerRepository.save(manager);
            return manager;
        } else
        throw new IllegalArgumentException("There can only be one manager in the system");

    }

    /**
     * Returns a set of all employees currently in the system
     *
     *
     * @author Tarek Namani
     * @return Set<Employee>
     */
    @Transactional
    public Set<Employee> getSetOfEmployees() {
        Iterable<Employee> employees = employeeRepository.findAll();
        if (employees == null || !employees.iterator().hasNext()) {
            throw new EntityNotFoundException("There are no employees in the system");
        }

        return StreamSupport.stream(employees.spliterator(), false).collect(Collectors.toSet()); //get a set of all employees
    }

    /**
     *
     * Finds an employee in the system using their username
     *
     *
     * @author Tarek Namani
     * @param username
     * @return Employee
     */
    @Transactional
    public Employee getEmployeeByUsername(String username) {
        if (!validateStringParameter(username)) {
            throw new IllegalArgumentException("Username cannot be empty, null, or contain spaces.");
        }

        return employeeRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Employee with this username does not exist."));
    }

    /**
     * Returns the manager in the system
     *
     *
     * @author Tarek Namani
     * @return Manager
     */
    @Transactional
    public Manager getManager() {

        Iterable<Manager> managers = managerRepository.findAll();
        if (managers == null || !managers.iterator().hasNext()) {
            throw new EntityNotFoundException("Manager does not exist");
        }
        return managers.iterator().next();

    }

    /**
     * Returns a set of all customers currently in the system
     *
     * @author Tarek Namani
     * @return Set<Customer>
     */
    @Transactional
    public Set<Customer> getSetOfCustomers() {
        Iterable<Customer> customers = customerRepository.findAll();
        if (customers == null || !customers.iterator().hasNext()) {
            throw new EntityNotFoundException("There are no customers in the system");
        }

        return StreamSupport.stream(customers.spliterator(), false).collect(Collectors.toSet()); //get a set of all employees
    }



    /**
     *
     * Finds an employee in the system using their username
     *
     * @author Tarek Namani
     * @param email
     * @return Employee
     */
    @Transactional
    public Customer getCustomerByEmail(String email) {
        if (!validateStringParameter(email)) {
            throw new IllegalArgumentException("Email cannot be empty, null, or contain spaces.");
        }

        return customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Customer with this email does not exist."));
    }


    /**
     * Set the activity an employee account by username
     * 
     * @param id
     * @param is_active the activity status of the employee
     * @return boolean
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public void setEmployeeStatus(int id, boolean is_active) {
        Employee employee = employeeRepository.findEmployeeById(id).orElseThrow(()-> new EntityNotFoundException("Employee does not exist"));
        employee.setActive(is_active);
        employeeRepository.save(employee);
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

        if (!validateStringParameter(email)) {
            throw new IllegalArgumentException("Email cannot be empty, null or contain spaces.");
        }
        if (!validateStringParameter(password)) {
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

        if (!validateStringParameter(username)) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }
        if (!validateStringParameter(password)) {
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

        if (!validateStringParameter(username)) {
            throw new IllegalArgumentException("Username cannot be empty, null or contain spaces.");
        }
        if (!validateStringParameter(password)) {
            throw new IllegalArgumentException("Password cannot be empty, null or contain spaces.");
        }


        Manager manager = managerRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("Manager does not exist"));
        if (!manager.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password!");
        }
        return manager;
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
        if (!validateStringParameter(oldPassword)) {
            throw new IllegalArgumentException("Old password cannot be empty, null or contain spaces.");
        }
        if (!validateStringParameter(newPassword)) {
            throw new IllegalArgumentException("New password cannot be empty, null or contain spaces.");
        }
        if (!validateStringParameter(email)) {
            throw new IllegalArgumentException("Email cannot be empty, null or contain spaces.");
        }

        Customer customer = customerRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Customer does not exist"));

        if (!customer.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        customer.setPassword(newPassword);
        customerRepository.save(customer);
        return customer;
    }

    /**
     * Update customer username
     * @param newUsername
     * @oaram customerEmail
     * @return Customer
     * 
     * @Author Ana Gordon
     */
    @Transactional
    public Customer updateCustomerUsername(String newUsername, String customerEmail) {
        if (!validateStringParameter(newUsername)) {
            throw new IllegalArgumentException("New username cannot be empty, null or contain spaces.");
        }
        if (!validateStringParameter(customerEmail)) {
            throw new IllegalArgumentException("Email cannot be empty, null or contain spaces.");
        }

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(()-> new EntityNotFoundException("Customer does not exist"));
        customer.setUsername(newUsername);
        customerRepository.save(customer);
        return customer;
    }

    /**
     * Update employee username
     * @param newUsername
     * @param oldUsername
     * @return Customer
     *
     * @Author Ana Gordon
     */
    @Transactional
    public Employee updateEmployeeUsername(String newUsername, String oldUsername) {
        if (!validateStringParameter(newUsername)) {
            throw new IllegalArgumentException("New username cannot be empty, null or contain spaces.");
        }
        if (!validateStringParameter(oldUsername)) {
            throw new IllegalArgumentException("Old username cannot be empty, null or contain spaces.");
        }

        if (employeeRepository.findByUsername(newUsername).isPresent()) {
            throw new EntityExistsException("Username is already in use by another employee");
        }

        Employee employee = employeeRepository.findByUsername(oldUsername).orElseThrow(()-> new EntityNotFoundException("Employee does not exist"));
        employee.setUsername(newUsername);
        employeeRepository.save(employee);
        return employee;
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
    public Customer updateCustomerPhoneNumber(String newPhoneNumber, String customerEmail) {
        if (!validateStringParameter(newPhoneNumber)) {
            throw new IllegalArgumentException("New phone number cannot be empty, null or contain spaces.");
        }

        if (!validateStringParameter(customerEmail)) {
            throw new IllegalArgumentException("Email cannot be empty, null or contain spaces.");
        }

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(() -> new EntityNotFoundException("Customer does not exist"));

        customer.setPhoneNumber(newPhoneNumber);
        customerRepository.save(customer);
        return customer;

    }

    @Transactional
    public void addGameToWishlist(int customerId, int gameId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        if (customer.addGameToWishlist(game)) {
            customerRepository.save(customer);
        }
    }

    @Transactional
    public void removeGameFromWishlist(int customerId, int gameId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        if (!customer.removeGameFromWishlist(game)) {
            throw new EntityNotFoundException("Game not in wishlist");
        }

        customerRepository.save(customer);
    }

    public Set<Game> viewWishlist(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return customer.getCopyWishlist();
    }
}
