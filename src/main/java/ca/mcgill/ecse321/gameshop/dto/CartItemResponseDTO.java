package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.CartItem;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link CartItem}
 */
public record CartItemResponseDTO(
        int id,
        String name,
        String description,
        String coverPicture,
        float price,
        boolean isActive,
        int stock,
        int quantity,
        Set<CategoryResponseDTO> categories,
        Set<PromotionResponseDTO> promotions) implements Serializable {

    public CartItemResponseDTO(CartItem cartItem) {
        this(cartItem.getGame().getId(), cartItem.getGame().getName(), cartItem.getGame().getDescription(), cartItem.getGame().getCoverPicture(), cartItem.getGame().getPrice(),
                cartItem.getGame().isActive(), cartItem.getGame().getStock(),
                cartItem.getQuantity(),
                cartItem.getGame().getCopyCategories().stream().map(CategoryResponseDTO::new).collect(Collectors.toSet()),
                cartItem.getGame().getCopyPromotions().stream().map(PromotionResponseDTO::new).collect(Collectors.toSet())
        );
    }
}
