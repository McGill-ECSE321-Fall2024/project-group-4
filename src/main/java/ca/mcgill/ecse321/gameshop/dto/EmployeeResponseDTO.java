package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Employee;

import java.util.Set;
import java.util.stream.Collectors;

public record EmployeeResponseDTO(int id, String username, String password, boolean isActive) {
    public EmployeeResponseDTO(Employee employee) {
        this(employee.getId(), employee.getUsername(), employee.getPassword(), employee.isActive()
                );
    }
    public Employee toEmployee() {
        return new Employee(this.username, this.password, this.isActive);
    }
}