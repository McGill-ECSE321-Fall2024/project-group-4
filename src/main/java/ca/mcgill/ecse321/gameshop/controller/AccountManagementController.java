package ca.mcgill.ecse321.gameshop.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.gameshop.dto.CustomerDTO;
import ca.mcgill.ecse321.gameshop.dto.EmployeeDTO;
import ca.mcgill.ecse321.gameshop.dto.ManagerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.gameshop.dto.GameDTO;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

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
    public List<GameDTO> viewWishlist(@PathVariable int customerId) {
        Set<Game> wishlist = accountManagementService.viewWishlist(customerId);
        return wishlist.stream().map(GameDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/customers/{email}")
    public CustomerDTO createCustomer(@PathVariable String email, @RequestBody String password, @RequestBody String username, @RequestBody String phoneNumber) {
        return new CustomerDTO(accountManagementService.createCustomer(email,password,username,phoneNumber));
    }

    @PostMapping("/employees/{username}")
    public EmployeeDTO createEmployee(@PathVariable String username, @RequestBody String password, @RequestBody boolean is_active) {
        return new EmployeeDTO(accountManagementService.createEmployee(username,password,is_active));
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

    @GetMapping("/login/customers/{customerEmail}")
    public CustomerDTO customerLogin(@PathVariable String customerEmail, @RequestBody String password) {
        return new CustomerDTO(accountManagementService.customerLogin(customerEmail,password));
    }

    @GetMapping("/login/employees/{username}")
    public EmployeeDTO employeeLogin(@PathVariable String username, @RequestBody String password) {
        return new EmployeeDTO(accountManagementService.employeeLogin(username,password));
    }

    @GetMapping("/login/manager/{username}")
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

    @PutMapping("/login/employees/{oldUsername}")
    public EmployeeDTO updateEmployeeUsername(@PathVariable String oldUsername, @RequestBody String newUsername) {
        return new EmployeeDTO(accountManagementService.updateEmployeeUsername(newUsername,oldUsername));
    }















}

