package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.persistence.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
}