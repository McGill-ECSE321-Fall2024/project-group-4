package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    @Override
    Iterable<Review> findAll();

    Set<Review> findByPurchase_GamePurchased_Id(int id);
}
