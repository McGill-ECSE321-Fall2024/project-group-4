package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.persistence.Policy;
import ca.mcgill.ecse321.gameshop.DAO.PolicyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestPolicy {

    @Autowired
    private PolicyRepository policyRepository;

    @AfterEach
    public void clearDatabase() {policyRepository.deleteAll();}


    @Test
    void testCreatePolicy() {
        //create policy
        Policy policy = new Policy("Nothing allowed");


        //save policy
        policy = policyRepository.save(policy);
        int id = policy.getId();

        //read from database
        Policy loadedPolicy = policyRepository.findById(id).orElse(null);


        //Assert correct response
        assertNotNull(loadedPolicy);
        assertEquals(policy.getDescription(), loadedPolicy.getDescription());


    }
    @Test
    void testUpdatePolicy() {
        //create and save policy
        Policy policy = new Policy("Nothing allowed");
        policy = policyRepository.save(policy);
        int id = policy.getId();

        //save policy
        policy.setDescription("Updated description");
        policy = policyRepository.save(policy);

        Policy loadedPolicy = policyRepository.findById(id).orElse(null);
        assertNotNull(loadedPolicy);
        assertEquals(1, policyRepository.count());
        assertEquals(policy.getDescription(), loadedPolicy.getDescription());

    }

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
