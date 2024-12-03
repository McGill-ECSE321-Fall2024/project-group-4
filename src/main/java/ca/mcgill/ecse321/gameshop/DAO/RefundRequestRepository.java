package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.RefundRequest;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public interface RefundRequestRepository extends CrudRepository<RefundRequest, Long> {
    Optional<RefundRequest> findById(int id);

    Set<RefundRequest> findByReviewer_Id(int id);
}
