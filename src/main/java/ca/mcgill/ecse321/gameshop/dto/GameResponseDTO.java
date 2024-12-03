package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Game;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link ca.mcgill.ecse321.gameshop.model.Game}
 */
public record GameResponseDTO(
        int id,
        String name,
        String description,
        String coverPicture,
        float price,
        boolean isActive,
        int stock,
        Set<CategoryResponseDTO> categories,
        Set<PromotionResponseDTO> promotions

) implements Serializable {

    public GameResponseDTO(Game game) {
        this(game.getId(), game.getName(), game.getDescription(), game.getCoverPicture(), game.getPrice(),
                game.isActive(), game.getStock(),
                game.getCopyCategories().stream().map(CategoryResponseDTO::new).collect(Collectors.toSet()),
                game.getCopyPromotions().stream().map(PromotionResponseDTO::new).collect(Collectors.toSet())
        );
    }
}
