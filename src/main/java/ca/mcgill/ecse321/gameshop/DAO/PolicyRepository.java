package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.Policy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends CrudRepository<Policy, Integer> {

}
