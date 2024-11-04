package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.EmployeeRepository;
import ca.mcgill.ecse321.gameshop.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Author : Tarek Namani
 */
@SpringBootTest
public class TestEmployee {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;


    @AfterEach
    public void tearDown() {
        employeeRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        employee = new Employee("Bob joe", "Unsafe Password", true);
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
        Optional<Employee> loadedEmployeeOpt = employeeRepository.findById(employee.getId());

        //compare
        assertTrue(loadedEmployeeOpt.isPresent());
        Employee loadedEmployee = loadedEmployeeOpt.get();
        assertEquals(employee.getId(),loadedEmployee.getId());
        assertEquals(employee.getPassword(),loadedEmployee.getPassword());
        assertEquals(employee.getUsername(),loadedEmployee.getUsername());
        assertEquals(employee.isActive(),loadedEmployee.isActive());
    }
}
