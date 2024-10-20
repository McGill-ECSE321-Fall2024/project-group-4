package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.RefundRequest;
import org.springframework.data.repository.CrudRepository;

public interface RefundRequestRepository extends CrudRepository<RefundRequest, Long> {
}
