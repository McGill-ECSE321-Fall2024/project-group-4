package ca.mcgill.ecse321.gameshop.DAO;

import ca.mcgill.ecse321.gameshop.model.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface CartItemRepository extends CrudRepository<CartItem, CartItem.CartItemId> {
    Set<CartItem> findByCartItemId_Customer_Id(int id);
    Set<CartItem> findByCartItemId_Game_Id(int id);
    Optional<CartItem> findByCartItemId_Customer_IdAndCartItemId_Game_Id(int customerId, int gameId);
}