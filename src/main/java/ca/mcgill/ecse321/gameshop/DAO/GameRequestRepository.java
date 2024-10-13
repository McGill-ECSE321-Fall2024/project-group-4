package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.GameRequest;
import org.springframework.data.repository.CrudRepository;

public interface GameRequestRepository extends CrudRepository<GameRequest, Integer> {
}
