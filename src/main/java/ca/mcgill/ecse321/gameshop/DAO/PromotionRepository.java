package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.persistence.Promotion;
import org.springframework.data.repository.CrudRepository;

public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    Promotion findById(int id);
    void deleteById(int id);
}
