package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.persistence.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);
    List<Customer> findByPhoneNumber(String phoneNumber);
}
