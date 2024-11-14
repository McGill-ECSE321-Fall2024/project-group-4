package ca.mcgill.ecse321.gameshop.dto;



import java.util.List;

public record GameInputDTO(
        String name,
        String description,
        String coverPicture,
        float price,
        boolean isActive,
        int stock,
        List<String> categories
) {
}
