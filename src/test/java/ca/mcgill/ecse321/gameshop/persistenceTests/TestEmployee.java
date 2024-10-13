package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;
import ca.mcgill.ecse321.gameshop.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * Author : Tarek Namani
 */
@SpringBootTest
public class TestEmployee {

    @Autowired
    private EmployeeRepository employeeRepository;

    Employee employee = new Employee();


    @AfterEach
    public void tearDown() {
        employeeRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setActive(true);
        employee.setPassword("Unsafe Password");
        employee.setUsername("Bob joe");
    }
    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading a Employee object from the database
     */
    @Test
    public void testSaveAndLoadEmployee() {
        //save
        employeeRepository.save(employee);

        //load
        Employee loadedEmployee = employeeRepository.findById(employee.getId());

        //compare
        assertEquals(employee.getId(),loadedEmployee.getId());
        assertEquals(employee.getPassword(),loadedEmployee.getPassword());
        assertEquals(employee.getUsername(),loadedEmployee.getUsername());
        assertEquals(employee.isActive(),loadedEmployee.isActive());
    }
}
