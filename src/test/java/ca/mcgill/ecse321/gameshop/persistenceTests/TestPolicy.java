package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.model.Policy;
import ca.mcgill.ecse321.gameshop.DAO.PolicyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author : Tarek Namani
 */
@SpringBootTest
public class TestPolicy {

    @Autowired
    private PolicyRepository policyRepository;

    @AfterEach
    public void clearDatabase() {policyRepository.deleteAll();}


    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading a Policy object from the database
     */
    @Test
    void testSaveAndLoadPolicy() {
        //create policy
        Policy policy = new Policy("Nothing allowed");


        //save policy
        policy = policyRepository.save(policy);
        int id = policy.getId();

        //read from database
        Optional<Policy> loadedPolicyOpt = policyRepository.findById(id);


        //Assert correct response
        assertTrue(loadedPolicyOpt.isPresent());
        assertEquals(policy.getDescription(), loadedPolicyOpt.get().getDescription());


    }

    //extra functionality
    @Test
    void testUpdatePolicy() {
        //create and save policy
        Policy policy = new Policy("Nothing allowed");
        policy = policyRepository.save(policy);
        int id = policy.getId();

        //save policy
        policy.setDescription("Updated description");
        policy = policyRepository.save(policy);

        Optional<Policy> loadedPolicyOpt = policyRepository.findById(id);
        assertTrue(loadedPolicyOpt.isPresent());
        assertEquals(1, policyRepository.count());
        assertEquals(policy.getDescription(), loadedPolicyOpt.get().getDescription());

    }
    //extra functionality
    @Test
    void testDeletePolicy() {
        //create and save policy
        Policy policy = new Policy("Nothing allowed");
        policy = policyRepository.save(policy);
        int id = policy.getId();

        //delete policy
        policyRepository.deleteById(id);


        assertEquals(0, policyRepository.count());

    }
}
