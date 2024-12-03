package ca.mcgill.ecse321.gameshop.controller;

import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.model.Policy;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8087")
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
    @PutMapping("/customers/{customerId}/wishlist/{gameId}")
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
    @DeleteMapping("/customers/{customerId}/wishlist/{gameId}")
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
    @GetMapping("/customers/{customerId}/wishlist")
    public List<GameResponseDTO> viewWishlist(@PathVariable int customerId) {
        Set<Game> wishlist = accountManagementService.viewWishlist(customerId);
        return wishlist.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    /**
     * Create a customer account
     *
     * @param customerDTO
     * @return CustomerDTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/customers")
    public CustomerResponseDTO createCustomer(@RequestBody CustomerRequestDTO customerDTO) {
        return new CustomerResponseDTO(accountManagementService.createCustomer(customerDTO.email(), customerDTO.password(),
                customerDTO.username(), customerDTO.phoneNumber()));
    }

    /**
     * Get the list of all the customer accounts
     *
     * @return List of all the customers
     *
     * @author Tarek Namani
     */
    @GetMapping("/customers")
    public Set<CustomerResponseDTO> getCustomers() {
        return accountManagementService.getSetOfCustomers().stream().map(CustomerResponseDTO::new).collect(Collectors.toSet());
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
    public CustomerResponseDTO getCustomerByEmail(@PathVariable String customerEmail) {
        return new CustomerResponseDTO(accountManagementService.getCustomerByEmail(customerEmail));
    }

    @GetMapping("/customers/ids/{customerId}")
    public CustomerResponseDTO getCustomerById(@PathVariable int customerId) {
        return new CustomerResponseDTO(accountManagementService.getCustomerById(customerId));
    }

    /**
     *  Create an employee account
     *
     * @param employeeDTO
     * @return EmployeeDTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/employees")
    public EmployeeResponseDTO createEmployee(@RequestBody EmployeeRequestDTO employeeDTO) {
        return new EmployeeResponseDTO(accountManagementService.createEmployee(employeeDTO.username(),
                employeeDTO.password(), employeeDTO.isActive()));
    }

    /**
     * Create the manager account
     *
     * @return The manager DTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/manager")
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
    @GetMapping("/employees")
    public Set<EmployeeResponseDTO> getEmployees() {
        return accountManagementService.getSetOfEmployees().stream().map(EmployeeResponseDTO::new).collect(Collectors.toSet());
    }

    /**
     * Return the employee with the given id
     *
     * @param employeeId Employee unique identifier
     * @return Employee DTO corresponding to the unique identifier
     *
     * @author Camille Pouliot
     */
    @GetMapping("/employees/id/{employeeId}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable int employeeId) {
        return new EmployeeResponseDTO(accountManagementService.findEmployeeById(employeeId));
    }

    /**
     * Get an employee from username
     *
     * @param username Unique username of an employee
     * @return Employee DTO with the username
     *
     * @author Tarek Namani
     */
    @GetMapping("/employees/username/{username}")
    public EmployeeResponseDTO getEmployeeByUsername(@PathVariable String username) {
        return new EmployeeResponseDTO(accountManagementService.getEmployeeByUsername(username));
    }


    /**
     * Set the status of an employee as active/inactive
     *
     * @param id Employee unique identifier
     * @param status Updated status of the employee
     *
     * @author Tarek Namani
     */
    @PutMapping("/employees/{id}/active/{status}")
    public void setEmployeeActivityById(@PathVariable int id, @PathVariable boolean status) {
        accountManagementService.setEmployeeStatus(id, status);
    }

    @PostMapping("/login/customers/{customerEmail}")
    public CustomerResponseDTO customerLogin(@PathVariable String customerEmail, @RequestBody String password) {
        return new CustomerResponseDTO(accountManagementService.customerLogin(customerEmail,password));
    }

    @PostMapping("/login/employees/{username}")
    public EmployeeResponseDTO employeeLogin(@PathVariable String username, @RequestBody String password) {
        return new EmployeeResponseDTO(accountManagementService.employeeLogin(username,password));
    }

    @PostMapping("/login/manager/{username}")
    public ManagerDTO managerLogin(@PathVariable String username, @RequestBody String password) {
        return new ManagerDTO(accountManagementService.managerLogin(username,password));
    }

    /**
     * Change a customer password
     *
     * @param customerEmail
     * @param changePasswordDTO
     * @return CustomerDTO
     *
     * @author Tarek Namani
     */
    @PutMapping("/customers/{customerEmail}/password")
    public CustomerResponseDTO changeCustomerPassword(@PathVariable String customerEmail, @RequestBody ChangePasswordDTO changePasswordDTO) {
        return new CustomerResponseDTO(accountManagementService.updateCustomerPassword(changePasswordDTO.oldPassword(),
                changePasswordDTO.newPassword(),customerEmail));
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
    @PutMapping("/customers/{customerEmail}/username/{newUsername}")
    public CustomerResponseDTO updateCustomerUsername(@PathVariable String customerEmail, @PathVariable String newUsername) {
        return new CustomerResponseDTO(accountManagementService.updateCustomerUsername(newUsername,customerEmail));
    }

    /**
     * Change a customer phone number
     *
     * @param customerEmail Customer unique email
     * @param newPhoneNumber Updated phone number
     * @return Customer DTO of the updated customer
     *
     * @author Tarek Namani
     */
    @PutMapping("/customers/{customerEmail}/phoneNumber/{newPhoneNumber}")
    public CustomerResponseDTO changeCustomerPhoneNumber(@PathVariable String customerEmail, @PathVariable String newPhoneNumber) {
        return new CustomerResponseDTO(accountManagementService.updateCustomerPhoneNumber(newPhoneNumber,customerEmail));
    }

    @PutMapping("/employees/{oldUsername}/username/{newUsername}")
    public EmployeeResponseDTO updateEmployeeUsername(@PathVariable String oldUsername, @PathVariable String newUsername) {
        return new EmployeeResponseDTO(accountManagementService.updateEmployeeUsername(newUsername,oldUsername));
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
     * @param description description of policy to add
     * @return Policy DTO of the created policy, different from the param
     *
     * @author Camille Pouliot
     */
    @PostMapping("/policies")
    public PolicyDTO createPolicy(@RequestBody String description) {
        Policy createdPolicy = accountManagementService.createPolicy(description);
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
    @PutMapping("/policies/{policyId}")
    public PolicyDTO updatePolicy(@PathVariable int policyId, @RequestBody String description) {
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
     * @param customerEmail of customer
     * @return AddressDTO
     *
     * @author Tarek Namani
     */
    @PostMapping("/customers/{customerEmail}/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDTO createAddress(@PathVariable String customerEmail, @RequestBody AddressRequestDTO address) {
        return new AddressResponseDTO(accountManagementService.createAddress(address.street(), address.city(), address.province(),
                address.postalCode(), address.country(), customerEmail));
    }
}

