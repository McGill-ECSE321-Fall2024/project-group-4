package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Customer findByEmail(String email);
    List<Customer> findByPhoneNumber(String phoneNumber);
}
