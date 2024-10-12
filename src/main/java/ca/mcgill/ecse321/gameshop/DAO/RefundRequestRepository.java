package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.persistence.RefundRequest;
import org.springframework.data.repository.CrudRepository;

public interface RefundRequestRepository extends CrudRepository<RefundRequest, Long> {
    RefundRequest findRefundRequestById(Long id);
}
