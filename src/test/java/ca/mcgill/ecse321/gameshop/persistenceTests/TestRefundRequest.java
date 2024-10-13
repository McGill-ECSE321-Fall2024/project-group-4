package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;

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
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    private RefundRequest refundRequest;
    private Employee employee = new Employee("employee", "password", true);
    private Customer customer = new Customer("customer", "password", "customer@email.com", "0123456789");
    private Address address = new Address("street", "city", "province", "country", "1A2 B3C", customer);
    private CreditCard creditCard = new CreditCard(1234, 123, LocalDate.now(), customer, address);
    private Game game = new Game("game", "description", "picture", 12, true, 1);
    private Purchase purchase = new Purchase(LocalDate.now(), 12, game, customer, address, creditCard);

    @AfterEach
    public void tearDown() {
        refundRequestRepository.deleteAll();
        purchaseRepository.deleteAll();
        gameRepository.deleteAll();
        creditCardRepository.deleteAll();
        addressRepository.deleteAll();
        employeeRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        refundRequest = new RefundRequest();
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
        employeeRepository.save(employee);
        customerRepository.save(customer);
        addressRepository.save(address);
        creditCardRepository.save(creditCard);
        gameRepository.save(game);
        purchaseRepository.save(purchase);


        purchase.setRefundRequest(refundRequest);
        refundRequestRepository.save(refundRequest);
        purchaseRepository.save(purchase);

        //load
        RefundRequest loadedRefundRequest = refundRequestRepository.findRefundRequestById(refundRequest.getId());

        //compare
        assertNotNull(refundRequest);
        assertEquals(refundRequest.getReason(), loadedRefundRequest.getReason());
        assertEquals(refundRequest.getReviewer().getId(), loadedRefundRequest.getReviewer().getId());
        assertEquals(refundRequest.getPurchase().getId(), loadedRefundRequest.getPurchase().getId());
        assertEquals(refundRequest.getStatus(), loadedRefundRequest.getStatus());
    }
}
