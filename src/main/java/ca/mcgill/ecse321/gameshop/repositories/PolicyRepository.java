package ca.mcgill.ecse321.gameshop.repositories;

import ca.mcgill.ecse321.gameshop.persistence.Policy;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PolicyRepository extends CrudRepository<Policy, Integer> {
    void deleteById(int id);

}
