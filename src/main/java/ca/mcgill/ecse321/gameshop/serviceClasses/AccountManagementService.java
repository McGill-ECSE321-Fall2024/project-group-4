package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Service class for the Account entity
 * 
 * @author Ana Gordon, Tarek Namani, Clara Mickail, Camille Pouliot
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

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private PurchaseManagementService purchaseManagementService;

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
        return inputString != null && !inputString.trim().isEmpty() && !inputString.contains(" ");
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

        Customer customer = new Customer(username, passwordEncoder.encode(password), email, phoneNumber);
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

        if (employeeRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Employee already exists with that username.");
        }
        Employee employee = new Employee(username, passwordEncoder.encode(password), is_active);
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
        if (managers.iterator().hasNext()) {
            throw new IllegalArgumentException("There can only be one manager in the system");
        }

        Manager manager = new Manager("manager", passwordEncoder.encode("manager"));
        managerRepository.save(manager);
        return manager;
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
        /*if (!employees.iterator().hasNext()) {
            return Collections.emptySet();
        }*/

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

    @Transactional
    public Customer getCustomerById(int customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer with this ID does not exist."));
    }


    /**
     * Set the activity an employee account by username
     * 
     * @param id
     * @param is_active the activity status of the employee
     * @return boolean
     * 
     * @author Ana Gordon
     */
    @Transactional
    public void setEmployeeStatus(int id, boolean is_active) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Employee does not exist"));
        employee.setActive(is_active);
        employeeRepository.save(employee);
    }

    /**
     * Get an employee from id
     *
     * @param id
     * @return Employee
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Employee findEmployeeById(int id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        }
        throw new IllegalArgumentException("No Employee found");
    }


    /**
     * Login to a customer account
     * 
     * @param email
     * @param password
     * @return Customer
     * 
     * @author Ana Gordon
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
        if (!passwordEncoder.matches(password, customer.getPassword())) {
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
     * @author Tarek Namani
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
        if (!passwordEncoder.matches(password, employee.getPassword())) {
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
     * @author Tarek Namani
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
        if (!passwordEncoder.matches(password, manager.getPassword())) {
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
     * @author Ana Gordon
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

        if (!passwordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
        return customer;
    }

    /**
     * Update customer username
     * @param newUsername
     * @oaram customerEmail
     * @return Customer
     * 
     * @author Ana Gordon
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
     * @author Ana Gordon
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
     * @author Ana Gordon
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

    /**
     * Add a game to a customer wishlist
     *
     * @param customerId
     * @param gameId
     *
     * @author Clara Mickail
     */
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

    /**
     * Remove a game from a customer wishlist
     *
     * @param customerId
     * @param gameId
     *
     * @author Clara Mickail
     */
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

    /**
     * Get a customer wishlist
     *
     * @param customerId
     * @return Set<Game>
     *
     * @author Clara Mickail
     */
    @Transactional
    public Set<Game> viewWishlist(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return customer.getCopyWishlist();
    }

    /**
     * Get a policy from id
     *
     * @param policyId
     * @return Policy
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Policy findPolicyById(int policyId) {
        return policyRepository.findById(policyId).orElseThrow(() -> new EntityNotFoundException("Policy not found"));
    }

    /**
     * Creates a new policy
     *
     * @param description
     * @return Policy
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Policy createPolicy(String description) {
        if(description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty, null or contain only spaces.");
        }

        Policy policy = new Policy(description);
        policyRepository.save(policy);
        return policy;
    }

    /**
     * Update a policy's fields
     *
     * @param policyId
     * @param description
     * @return Policy
     *
     * @author Camille Pouliot
     */
    @Transactional
    public Policy updatePolicy(int policyId, String description) {
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty, null or contain only spaces.");
        }

        Policy policy = findPolicyById(policyId);
        policy.setDescription(description);
        policyRepository.save(policy);
        return policy;
    }

    /**
     * Delete a policy
     *
     * @param policyId
     *
     * @author Camille Pouliot
     */
    @Transactional
    public void deletePolicy(int policyId) {
        Policy policy = findPolicyById(policyId);
        policyRepository.delete(policy);
    }

    /**
     *
     * Creates an address and adds it to a customer
     *
     * @param street of the address
     * @param city of the address
     * @param province of the address
     * @param zip of the address
     * @param country of the address
     * @param customerEmail of customer residing at the address
     * @author Tarek Namani
     * @throws IllegalArgumentException if any of the string fields are invalid
     * @throws EntityNotFoundException if the customer does not exist
     * @return the created address
     */
    @Transactional
    public Address createAddress(String street, String city, String province, String zip, String country, String customerEmail) {

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (street == null || street.isEmpty() || city == null || city.isEmpty() || province == null
                || province.isEmpty() || zip == null || zip.isEmpty() || country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Address contains null or empty strings");
        }
        Address customerAddress = new Address(street, city, province, country,zip,customer);

        addressRepository.save(customerAddress);
        customerRepository.save(customer);

        return customerAddress;

    }


    @Transactional
    public void removeAddressFromCustomer(String customerEmail, int addressId) {
        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        if (!customer.getCopyAddresses().stream().map(address -> address.getId()).collect(Collectors.toSet()).contains(addressId)) {
            throw new EntityNotFoundException("Address does not exist under customer");
        }
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new EntityNotFoundException("Address not found"));
        customer.removeAddress(address);
        address.deactivateAddress();
        Set<CreditCard> associatedCreditCards = new HashSet<>();
        creditCardRepository.findAll().forEach(associatedCreditCards::add);

        addressRepository.save(address);
        customerRepository.save(customer);


    }

    

    @Transactional
    public List<Policy> getAllPolicies() {
        return StreamSupport.stream(policyRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }




}
