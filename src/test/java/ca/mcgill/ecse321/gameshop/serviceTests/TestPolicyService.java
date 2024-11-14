package ca.mcgill.ecse321.gameshop.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.AccountManagementService;

/**
 * Test for policy service
 *
 * @author Camille Pouliot
 */
@SpringBootTest
public class TestPolicyService {

    @InjectMocks
    private AccountManagementService accountManagementService;

    @Mock
    private PolicyRepository policyRepository;

    Policy policy;

    @BeforeEach
    public void setUp() {
        policy = new Policy("Policy Description");

        when(policyRepository.save(any())).thenReturn(policy);
        when(policyRepository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(policyRepository.findById(-1)).thenReturn(Optional.empty());
    }

    @AfterEach
    public void tearDown() {
        policyRepository.deleteAll();
    }

    /**
     * Test finding a policy
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindPolicy() {
        //Act
        Policy loadedPolicy = accountManagementService.findPolicyById(policy.getId());

        //Assert
        assertNotNull(loadedPolicy);
        assertEquals(policy.getId(), loadedPolicy.getId());
        assertEquals("Policy Description", loadedPolicy.getDescription());
        verify(policyRepository, times(1)).findById(policy.getId());
    }

    /**
     * Test finding a policy with an invalid id
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindInvalidPolicy() {
        //Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountManagementService.findPolicyById(-1));

        //Assert
        assertEquals("Policy not found", exception.getMessage());
        verify(policyRepository, times(1)).findById(-1);
    }

    /**
     * Test creating a policy
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreatePolicy() {
        //Act
        Policy createdPolicy = accountManagementService.createPolicy("Policy Description");

        //Assert
        assertNotNull(createdPolicy);
        assertEquals("Policy Description", createdPolicy.getDescription());
        verify(policyRepository, times(1)).save(createdPolicy);
    }

    /**
     * Test creating a policy with an invalid description
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreateInvalidPolicy() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.createPolicy(""));

        //Assert
        assertEquals("Description cannot be empty, null or contain only spaces.", exception.getMessage());
    }

    /**
     * Test updating a policy
     *
     * @author Camille Pouliot
     */
    @Test
    public void testUpdatePolicy() {
        //Act
        Policy loadedPolicy = accountManagementService.updatePolicy(policy.getId(), "New Description");

        //Assert
        assertNotNull(loadedPolicy);
        assertEquals("New Description", loadedPolicy.getDescription());
        verify(policyRepository, times(1)).findById(policy.getId());
        verify(policyRepository, times(1)).save(policy);
    }

    /**
     * Test updating a policy with an invalid description
     *
     * @author Camille Pouliot
     */
    @Test
    public void testUpdateInvalidPolicy() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountManagementService.updatePolicy(policy.getId(), ""));

        //Assert
        assertEquals("Description cannot be empty, null or contain only spaces.", exception.getMessage());
    }

    /**
     * Test deleting a policy
     *
     * @author Camille Pouliot
     */
    @Test
    public void testDeletePolicy() {
        //Act
        accountManagementService.deletePolicy(policy.getId());

        //Verify
        verify(policyRepository, times(1)).delete(policy);
    }
}
