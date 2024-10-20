package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.Promotion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
}
