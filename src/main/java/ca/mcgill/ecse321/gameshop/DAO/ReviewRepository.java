package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.persistence.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Review findById(int id);
}