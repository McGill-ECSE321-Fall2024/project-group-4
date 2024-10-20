package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
