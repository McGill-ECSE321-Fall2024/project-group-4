package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Employee;
import ca.mcgill.ecse321.gameshop.model.GameRequest;
import ca.mcgill.ecse321.gameshop.model.RefundRequest;

import java.util.Set;
import java.util.stream.Collectors;

public record EmployeeDTO(int id, String username, String password, boolean isActive, Set<GameRequestDTO> gameReuqests, Set<RefundRequestDTO> refundRequests) {
    public EmployeeDTO(Employee employee) {
        this(employee.getId(), employee.getUsername(), employee.getPassword(), employee.isActive(),
                employee.getCopyGameRequests().stream().map((GameRequest t) -> new GameRequestDTO(t)).collect(Collectors.toSet()),
                employee.getCopyRefundRequests().stream().map(RefundRequestDTO::new).collect(Collectors.toSet()));
    }
}
