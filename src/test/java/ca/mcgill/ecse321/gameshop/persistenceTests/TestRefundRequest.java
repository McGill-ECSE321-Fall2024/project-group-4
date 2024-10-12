package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.persistence.Purchase;
import ca.mcgill.ecse321.gameshop.persistence.RequestStatus;
import ca.mcgill.ecse321.gameshop.persistence.Employee;
import ca.mcgill.ecse321.gameshop.persistence.RefundRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Author: Camille Pouliot
 */
@SpringBootTest
public class TestRefundRequest {
    @Autowired
    private RefundRequestRepository refundRequestRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    private RefundRequest refundRequest;
    private Employee employee;
    private Purchase purchase;

    @AfterEach
    public void tearDown() {
        refundRequestRepository.deleteAll();
        employeeRepository.deleteAll();
        purchaseRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        refundRequest = new RefundRequest();
        employee = new Employee();
        purchase = new Purchase();
        refundRequest.setPurchase(purchase);
        refundRequest.setReason("reason");
        refundRequest.setStatus(RequestStatus.APPROVED);
        refundRequest.setReviewer(employee);
    }

    @Test
    /**
     * Author: Camille Pouliot
     * Description: Tests saving and loading a RefundRequest object from the database
     */
    public void testSaveAndLoadRefundRequest() {
        //save
        purchaseRepository.save(purchase);
        refundRequestRepository.save(refundRequest);

        //load
        RefundRequest loadedRefundRequest = refundRequestRepository.findRefundRequestById(refundRequest.getId);

        //compare
        assertNotNull(refundRequest);
        assertEquals(refundRequest.getReason(), loadedRefundRequest.getReason());
        assertEquals(refundRequest.getReviewer().getId(), loadedRefundRequest.getReviewer().getId());
        assertEquals(refundRequest.getPurchase().getId(), loadedRefundRequest.getPurchase().getId());
        assertEquals(refundRequest.getStatus(), loadedRefundRequest.getStatus());
    }
}
