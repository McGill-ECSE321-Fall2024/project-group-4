package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.Account;
import ca.mcgill.ecse321.gameshop.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    Optional<Employee> findEmployeeById(int employeeID);
    Optional<Employee> findByUsername(String username);

}
