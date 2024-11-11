package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Category;

/**
 * DTO for {@link ca.mcgill.ecse321.gameshop.model.Category}
 */
public record CategoryDTO (
        int id,
        String name
){
    public CategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}
