package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.Purchase;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Address;
import ca.mcgill.ecse321.gameshop.model.CreditCard;
import ca.mcgill.ecse321.gameshop.model.Review;
import ca.mcgill.ecse321.gameshop.model.RefundRequest;
import ca.mcgill.ecse321.gameshop.model.RequestStatus;
import ca.mcgill.ecse321.gameshop.model.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    private Purchase purchase;
    private Customer customer = new Customer("customer", "password", "customer@email.com", "0123456789");
    private Address address = new Address("street", "city", "province", "country", "postalCode", customer);
    private CreditCard creditCard = new CreditCard(4567, "123", LocalDate.now(), customer, address);
    private Review review;
    private Employee employee = new Employee("employee", "password", true);
    private RefundRequest refundRequest;
    private Game game = new Game("game", "description", "coverPicture", 12, true, 1);

    @AfterEach
    public void tearDown() {
        refundRequestRepository.deleteAll();
        reviewRepository.deleteAll();
        purchaseRepository.deleteAll();
        creditCardRepository.deleteAll();
        addressRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
        gameRepository.deleteAll();

    }

    @BeforeEach
    public void setUp() {
        this.purchase = new Purchase(LocalDate.now(), 12f, game, customer, address, creditCard);
        this.review = new Review(3, "review", purchase);
        this.refundRequest = new RefundRequest(purchase, RequestStatus.PENDING, "reason", employee);
    }

    @Test
    @Transactional
    /**
     * Author: Camille Pouliot
     * Description: Tests saving and loading a Purchase object from the database
     */
    public void testSaveAndLoadPurchase() {
        //save
        customerRepository.save(customer);
        addressRepository.save(address);
        creditCardRepository.save(creditCard);
        employeeRepository.save(employee);
        gameRepository.save(game);
        purchaseRepository.save(purchase);

        purchase.setRefundRequest(refundRequest); //Because of database schema, we should not set the refund and review just yet, save purchase first
        purchase.setReview(review);
        refundRequestRepository.save(refundRequest); //then save them individually
        reviewRepository.save(review);

        purchaseRepository.save(purchase); //then update purchase with the new info


        //load
        Optional<Purchase> loadedPurchaseOpt = purchaseRepository.findById(purchase.getId());

        //compare
        assertTrue(loadedPurchaseOpt.isPresent());
        Purchase loadedPurchase = loadedPurchaseOpt.get();
        assertEquals(loadedPurchase.getCustomer().getUsername(), purchase.getCustomer().getUsername());
        assertEquals(loadedPurchase.getGamePurchased().getId(), purchase.getGamePurchased().getId());
        assertEquals(loadedPurchase.getReview().getId(), purchase.getReview().getId());
        assertEquals(loadedPurchase.getRefundRequest().getPurchase().getId(), purchase.getRefundRequest().getPurchase().getId());
        assertEquals(loadedPurchase.getDeliveryAddress().getId(), purchase.getDeliveryAddress().getId());
        assertEquals(loadedPurchase.getPurchasePrice(), purchase.getPurchasePrice());
        assertEquals(loadedPurchase.getPaymentMethod().getId(), purchase.getPaymentMethod().getId());
        assertEquals(loadedPurchase.getPurchaseDate(), purchase.getPurchaseDate());
    }




}
