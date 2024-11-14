package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Employee;

import java.util.Set;
import java.util.stream.Collectors;

public record EmployeeDTO(int id, String username, String password, boolean isActive, Set<RefundRequestDTO> refundRequests) {
    public EmployeeDTO(Employee employee) {
        this(employee.getId(), employee.getUsername(), employee.getPassword(), employee.isActive(),
                employee.getCopyRefundRequests().stream().map(RefundRequestDTO::new).collect(Collectors.toSet()));
    }
    public Employee toEmployee() {
        Employee employee = new Employee(this.username, this.password, this.isActive);
        return employee;
    }
}