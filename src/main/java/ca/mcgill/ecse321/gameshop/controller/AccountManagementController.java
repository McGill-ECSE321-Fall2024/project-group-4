package ca.mcgill.ecse321.gameshop.controller;

import ca.mcgill.ecse321.gameshop.dto.*;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.model.Policy;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.gameshop.model.Customer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountManagementController {

    @Autowired
    private AccountManagementService accountManagementService;

    @PutMapping("/{customerId}/wishlist/{gameId}")
    public void addGameToWishlist(@PathVariable int customerId, @PathVariable int gameId) {
        accountManagementService.addGameToWishlist(customerId, gameId);
    }

    @DeleteMapping("/{customerId}/wishlist/{gameId}")
    public void removeGameFromWishlist(@PathVariable int customerId, @PathVariable int gameId) {
        accountManagementService.removeGameFromWishlist(customerId, gameId);
    }

    @GetMapping("/{customerId}/wishlist")
    public List<GameResponseDTO> viewWishlist(@PathVariable int customerId) {
        Set<Game> wishlist = accountManagementService.viewWishlist(customerId);
        return wishlist.stream().map(GameResponseDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/customers/{email}")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return new CustomerDTO(accountManagementService.createCustomer(customerDTO.username(), customerDTO.password() ,customerDTO.email(),customerDTO.phoneNumber()));
    }

    @PostMapping("/employees/{username}")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return new EmployeeDTO(accountManagementService.createEmployee(employeeDTO.username(),employeeDTO.password(),employeeDTO.isActive()));
    }

    @PostMapping("/manager/")
    public ManagerDTO createManager() {
        return new ManagerDTO(accountManagementService.createManager());
    }

    @GetMapping("/employees/")
    public Set<EmployeeDTO> getEmployees() {
        return accountManagementService.getSetOfEmployees().stream().map(EmployeeDTO::new).collect(Collectors.toSet());
    }

    @GetMapping("/employees/{username}")
    public EmployeeDTO getEmployeeByUsername(@PathVariable String username) {
        return new EmployeeDTO(accountManagementService.getEmployeeByUsername(username));
    }

    @GetMapping("/customers/")
    public Set<CustomerDTO> getCustomers() {
        return accountManagementService.getSetOfCustomers().stream().map(CustomerDTO::new).collect(Collectors.toSet());
    }

    @GetMapping("/customers/{customerEmail}")
    public CustomerDTO getCustomerByEmail(@PathVariable String customerEmail) {
        return new CustomerDTO(accountManagementService.getCustomerByEmail(customerEmail));
    }

    @PutMapping("/employees/{id}/is_active/{status}")
    public void setEmployeeActivityById(@PathVariable int id, @PathVariable boolean status) {
        accountManagementService.setEmployeeStatus(id, status);
    }

    @PostMapping("/login/customers/{customerEmail}")
    public CustomerDTO customerLogin(@PathVariable String customerEmail, @RequestBody String password) {
        return new CustomerDTO(accountManagementService.customerLogin(customerEmail,password));
    }

    @PostMapping("/login/employees/{username}")
    public EmployeeDTO employeeLogin(@PathVariable String username, @RequestBody String password) {
        return new EmployeeDTO(accountManagementService.employeeLogin(username,password));
    }

    @PostMapping("/login/manager/{username}")
    public ManagerDTO managerLogin(@PathVariable String username, @RequestBody String password) {
        return new ManagerDTO(accountManagementService.managerLogin(username,password));
    }

    @PutMapping("/customers/{customerEmail}/password")
    public CustomerDTO changeCustomerPassword(@PathVariable String customerEmail, @RequestBody String oldPassword, @RequestBody String newPassword) {
        return new CustomerDTO(accountManagementService.updateCustomerPassword(oldPassword,newPassword,customerEmail));
    }

    @PutMapping("/customers/{customerEmail}/username")
    public CustomerDTO changeCustomerUsername(@PathVariable String customerEmail, @RequestBody String newUsername) {
        return new CustomerDTO(accountManagementService.updateCustomerUsername(newUsername,customerEmail));
    }

    @PutMapping("/customers/{customerEmail}/phoneNumber")
    public CustomerDTO changeCustomerPhonenumber(@PathVariable String customerEmail, @RequestBody String newPhonenumber) {
        return new CustomerDTO(accountManagementService.updateCustomerUsername(newPhonenumber,customerEmail));
    }

    @PutMapping("/employees/{oldUsername}")
    public EmployeeDTO updateEmployeeUsername(@PathVariable String oldUsername, @RequestBody String newUsername) {
        return new EmployeeDTO(accountManagementService.updateEmployeeUsername(newUsername,oldUsername));
    }

    /**
     * Get a policy from id
     *
     * @param policyId
     * @return PolicyDTO
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
     * @param policyDTO
     * @return PolicyDTO
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
     * @param policyId
     * @param description
     * @return PolicyDTO
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
     * @param policyId
     *
     * @author Camille Pouliot
     */
    @DeleteMapping("/policies/{policyId}")
    public void deletePolicy(@PathVariable int policyId) {
        accountManagementService.deletePolicy(policyId);
    }


    @PostMapping("/customers/{customerEmail}/addresses")
    public AddressDTO createAddress(@PathVariable String customerEmail, @RequestBody String zipCode, @RequestBody String city, @RequestBody String province, @RequestBody String country, @RequestBody String street) {
        return new AddressDTO(accountManagementService.createAddress(street,city,province,zipCode,country,customerEmail));
    }














}

