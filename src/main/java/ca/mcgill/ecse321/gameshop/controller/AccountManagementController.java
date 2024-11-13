package ca.mcgill.ecse321.gameshop.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;


/**
 * Methods for AccountManagementController
 *
 * @author Clara Mickail, Tarek Namani, Camille Pouliot
 */
@RestController
@RequestMapping("/accounts")
public class AccountManagementController {

    @Autowired
    private AccountManagementService accountManagementService;

    /**
     * Add a game to a customer wishlist
     *
     * @param customerId Customer unique identifier
     * @param gameId Game unique identifier to add to wishlist of the customer
     *
     * @author Clara Mickail
     */
    @PutMapping("/{customerId}/wishlist/{gameId}")
    public void addGameToWishlist(@PathVariable int customerId, @PathVariable int gameId) {
        accountManagementService.addGameToWishlist(customerId, gameId);
    }

    /**
     * Delete a game from a customer wishlist
     *
     * @param customerId Customer unique identifier
     * @param gameId Game unique identifier to remove from customer wishlist
     *
     * @author Clara Mickail
     */
    @DeleteMapping("/{customerId}/wishlist/{gameId}")
    public void removeGameFromWishlist(@PathVariable int customerId, @PathVariable int gameId) {
        accountManagementService.removeGameFromWishlist(customerId, gameId);
    }

    /**
     * Get the wishlist of a customer
     *
     * @param customerId Customer unique identifier
     * @return List of the games in the customer wishlist
     *
     * @author Tarek Namani
     */
    @GetMapping("/{customerId}/wishlist")
    public List<GameResponseDTO> viewWishlist(@PathVariable int customerId) {
        Set<Game> wishlist = accountManagementService.viewWishlist(customerId);
        return wishlist.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * Create a customer account
     *
     * @param email
     * @param password
     * @param username
     * @param phoneNumber
     * @return CustomerDTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/customers/{email}")
    public CustomerDTO createCustomer(@PathVariable String email, @RequestBody String password, @RequestBody String username, @RequestBody String phoneNumber) {
        return new CustomerDTO(accountManagementService.createCustomer(email,password,username,phoneNumber));
    }

    /**
     *  Create an employee account
     *
     * @param username
     * @param password
     * @param is_active
     * @return EmployeeDTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/employees/{username}")
    public EmployeeDTO createEmployee(@PathVariable String username, @RequestBody String password, @RequestBody boolean is_active) {
        return new EmployeeDTO(accountManagementService.createEmployee(username,password,is_active));
    }

    /**
     * Create the manager account
     *
     * @return The manager DTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/manager/")
    public ManagerDTO createManager() {
        return new ManagerDTO(accountManagementService.createManager());
    }

    /**
     * Get the list of all the employee accounts
     *
     * @return List of all employees
     *
     * @author Tarek Namani
     */
    @GetMapping("/employees/")
    public Set<EmployeeDTO> getEmployees() {
        return accountManagementService.getSetOfEmployees().stream().map(EmployeeDTO::new).collect(Collectors.toSet());
    }

    /**
     * Get an employee from username
     *
     * @param username Unique username of an employee
     * @return Employee DTO with the username
     *
     * @author Tarek Namani
     */
    @GetMapping("/employees/{username}")
    public EmployeeDTO getEmployeeByUsername(@PathVariable String username) {
        return new EmployeeDTO(accountManagementService.getEmployeeByUsername(username));
    }

    /**
     * Get the list of all the customer accounts
     *
     * @return List of all the customers
     *
     * @author Tarek Namani
     */
    @GetMapping("/customers/")
    public Set<CustomerDTO> getCustomers() {
        return accountManagementService.getSetOfCustomers().stream().map(CustomerDTO::new).collect(Collectors.toSet());
    }

    /**
     * Get a customer by their unique email
     *
     * @param customerEmail Customer unique email
     * @return Customer DTO with the email
     *
     * @author Tarek Namani
     */
    @GetMapping("/customers/{customerEmail}")
    public CustomerDTO getCustomerByEmail(@PathVariable String customerEmail) {
        return new CustomerDTO(accountManagementService.getCustomerByEmail(customerEmail));
    }

    /**
     * Set the status of an employee as active/inactive
     *
     * @param id Employee unique identifier
     * @param status Updated status of the employee
     *
     * @author Tarek Namani
     */
    @PutMapping("/employees/{id}/is_active/{status}")
    public void setEmployeeActivityById(@PathVariable int id, @PathVariable boolean status) {
        accountManagementService.setEmployeeStatus(id, status);
    }

    /**
     * Login into a customer account
     *
     * @param customerEmail Customer unique email
     * @param password Customer password
     * @return Customer DTO that logged in
     *
     * @author Tarek Namani
     */
    @GetMapping("/login/customers/{customerEmail}")
    public CustomerDTO customerLogin(@PathVariable String customerEmail, @RequestBody String password) {
        return new CustomerDTO(accountManagementService.customerLogin(customerEmail,password));
    }

    /**
     * Login into an employee account
     *
     * @param username Employee unique username
     * @param password Employee password
     * @return Employee DTO that logged in
     *
     * @author Tarek Namani
     */
    @GetMapping("/login/employees/{username}")
    public EmployeeDTO employeeLogin(@PathVariable String username, @RequestBody String password) {
        return new EmployeeDTO(accountManagementService.employeeLogin(username,password));
    }

    /**
     * Login into the manager account
     *
     * @param username Manager unique username
     * @param password Manager password
     * @return Manager DTO that logged in
     *
     * @author Tarek Namani
     */
    @GetMapping("/login/manager/{username}")
    public ManagerDTO managerLogin(@PathVariable String username, @RequestBody String password) {
        return new ManagerDTO(accountManagementService.managerLogin(username,password));
    }

    /**
     * Change a customer password
     *
     * @param customerEmail
     * @param oldPassword
     * @param newPassword
     * @return CustomerDTO
     *
     * @author Tarek Namani
     */
    @PutMapping("/customers/{customerEmail}/password")
    public CustomerDTO changeCustomerPassword(@PathVariable String customerEmail, @RequestBody String oldPassword, @RequestBody String newPassword) {
        return new CustomerDTO(accountManagementService.updateCustomerPassword(oldPassword,newPassword,customerEmail));
    }

    /**
     * Change a customer username
     *
     * @param customerEmail Customer unique email
     * @param newUsername Updated username of customer
     * @return Customer DTO of the updated customer
     *
     * @author Tarek Namani
     */
    @PutMapping("/customers/{customerEmail}/username")
    public CustomerDTO changeCustomerUsername(@PathVariable String customerEmail, @RequestBody String newUsername) {
        return new CustomerDTO(accountManagementService.updateCustomerUsername(newUsername,customerEmail));
    }

    /**
     * Change a customer phone number
     *
     * @param customerEmail Customer unique email
     * @param newPhonenumber Updated phone number
     * @return Customer DTO of the updated customer
     *
     * @author Tarek Namani
     */
    @PutMapping("/customers/{customerEmail}/phoneNumber")
    public CustomerDTO changeCustomerPhonenumber(@PathVariable String customerEmail, @RequestBody String newPhonenumber) {
        return new CustomerDTO(accountManagementService.updateCustomerUsername(newPhonenumber,customerEmail));
    }

    /**
     * Initial login into an employee account
     *
     * @param oldUsername
     * @param newUsername
     * @return EmployeeDTO
     *
     * @author Tarek Namani
     */
    @PutMapping("/login/employees/{oldUsername}")
    public EmployeeDTO updateEmployeeUsername(@PathVariable String oldUsername, @RequestBody String newUsername) {
        return new EmployeeDTO(accountManagementService.updateEmployeeUsername(newUsername,oldUsername));
    }

    /**
     * Get a policy from id
     *
     * @param policyId Policy unique identifier
     * @return Policy DTO corresponding to the id
     *
     * @author Camille Pouliot
     */
    @GetMapping("/policies/{policyId}")
    public PolicyDTO getPolicyById(@PathVariable int policyId) {
        return new PolicyDTO(accountManagementService.findPolicyById(policyId));
    }

    /**
     * Create a new policy
     *
     * @param policyDTO Policy DTO to add to the server
     * @return Policy DTO of the created policy, different from the param
     *
     * @author Camille Pouliot
     */
    @PostMapping("/policies")
    public PolicyDTO createPolicy(@RequestBody PolicyDTO policyDTO) {
        Policy createdPolicy = accountManagementService.createPolicy(policyDTO.description());
        return new PolicyDTO(createdPolicy);
    }

    /**
     * Update a policy's description
     *
     * @param policyId Policy unique identifier
     * @param description Updated description of policy
     * @return Policy DTO of the updated policy
     *
     * @author Camille Pouliot
     */
    @PutMapping("/policies/{policyId}/{description}")
    public PolicyDTO updatePolicy(@PathVariable int policyId, @PathVariable String description) {
        return new PolicyDTO(accountManagementService.updatePolicy(policyId,description));
    }

    /**
     * Delete a policy
     *
     * @param policyId Policy unique identifier
     *
     * @author Camille Pouliot
     */
    @DeleteMapping("/policies/{policyId}")
    public void deletePolicy(@PathVariable int policyId) {
        accountManagementService.deletePolicy(policyId);
    }


    /**
     * Create an address into a customer account
     *
     * @param customerEmail
     * @param zipCode
     * @param city
     * @param province
     * @param country
     * @param street
     * @return AddressDTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/customers/{customerEmail}/addresses")
    public AddressDTO createAddress(@PathVariable String customerEmail, @RequestBody String zipCode, @RequestBody String city, @RequestBody String province, @RequestBody String country, @RequestBody String street) {
        return new AddressDTO(accountManagementService.createAddress(street,city,province,zipCode,country,customerEmail));
    }














}

