package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Employee;

import java.util.Set;
import java.util.stream.Collectors;

public record EmployeeRequestDTO(String username, String password, boolean isActive, Set<RefundRequestDTO> refundRequests) {
    public EmployeeRequestDTO(Employee employee) {
        this(employee.getUsername(), employee.getPassword(), employee.isActive(),
                employee.getCopyRefundRequests().stream().map(RefundRequestDTO::new).collect(Collectors.toSet()));
    }
}