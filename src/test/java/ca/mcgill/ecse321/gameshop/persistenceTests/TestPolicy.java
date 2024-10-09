package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.persistence.Policy;
import ca.mcgill.ecse321.gameshop.repositories.PolicyRepository;
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
        Policy policy = new Policy();
        policy.setDescription("Nothing allowed");


        //save policy
        policy = policyRepository.save(policy);
        int id = policy.getId();

        //read from database
        Policy loadedPolicy = policyRepository.findById(id).get();


        //Assert correct response
        assertNotNull(loadedPolicy);
        assertEquals(policy.getDescription(), loadedPolicy.getDescription());


    }
}
