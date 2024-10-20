package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.Reply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends CrudRepository<Reply, Integer> {
}
