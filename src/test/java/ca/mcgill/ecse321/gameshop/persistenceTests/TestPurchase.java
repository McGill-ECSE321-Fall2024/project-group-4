package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.persistence.Purchase;
import ca.mcgill.ecse321.gameshop.persistence.Game;
import ca.mcgill.ecse321.gameshop.persistence.Customer;
import ca.mcgill.ecse321.gameshop.persistence.Address;
import ca.mcgill.ecse321.gameshop.persistence.CreditCard;
import ca.mcgill.ecse321.gameshop.persistence.Review;
import ca.mcgill.ecse321.gameshop.persistence.RefundRequest;
import ca.mcgill.ecse321.gameshop.persistence.RequestStatus;
import ca.mcgill.ecse321.gameshop.persistence.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Author: Camille Pouliot
 */
@SpringBootTest
public class TestPurchase {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RefundRequestRepository refundRequestRepository;

    private Purchase purchase = new Purchase();
    private Customer customer = new Customer("customer", "password", "customer@email.com", "0123456789");
    private Address address = new Address("street", "city", "province", "country", "postalCode", customer);
    private CreditCard creditCard = new CreditCard(4567, 123, LocalDate.now(), customer, address);
    private Review review = new Review(3, "review", purchase);
    private Employee employee = new Employee("employee", "password", true);
    private RefundRequest refundRequest = new RefundRequest(purchase, RequestStatus.PENDING, "reason", employee);
    private Game game = new Game("game", "description", "coverPicture", 12, true, 1);

    @AfterEach
    public void tearDown() {
        purchaseRepository.deleteAll();
        customerRepository.deleteAll();
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        addressRepository.deleteAll();
        creditCardRepository.deleteAll();
        employeeRepository.deleteAll();
        refundRequestRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        purchase.setCustomer(customer);
        purchase.setGamePurchased(game);
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setReview(review);
        purchase.setRefundRequest(refundRequest);
        purchase.setDeliveryAddress(address);
        purchase.setPurchasePrice(12);
        purchase.setPaymentMethod(creditCard);
    }

    @Test
    /**
     * Author: Camille Pouliot
     * Description: Tests saving and loading a Purchase object from the database
     */
    public void testSaveAndLoadPurchase() {
        //save
        employeeRepository.save(employee);
        customerRepository.save(customer);
        addressRepository.save(address);
        creditCardRepository.save(creditCard);
        gameRepository.save(game);
        purchaseRepository.save(purchase);
        refundRequestRepository.save(refundRequest);
        reviewRepository.save(review);

        //load
        Purchase loadedPurchase = purchaseRepository.findbyId(purchase.getId());

        //compare
        assertNotNull(purchase);
        assertEquals(loadedPurchase.getCustomer().getUsername(), purchase.getCustomer().getUsername());
        assertEquals(loadedPurchase.getGamePurchased().getId(), purchase.getGamePurchased().getId());
        assertEquals(loadedPurchase.getReview().getId(), purchase.getReview().getId());
        assertEquals(loadedPurchase.getRefundRequest().getPurchase(), purchase.getRefundRequest().getPurchase());
        assertEquals(loadedPurchase.getDeliveryAddress().getId(), purchase.getDeliveryAddress().getId());
        assertEquals(loadedPurchase.getPurchasePrice(), purchase.getPurchasePrice());
        assertEquals(loadedPurchase.getPaymentMethod().getId(), purchase.getPaymentMethod().getId());
        assertEquals(loadedPurchase.getPurchaseDate(), purchase.getPurchaseDate());
    }




}
