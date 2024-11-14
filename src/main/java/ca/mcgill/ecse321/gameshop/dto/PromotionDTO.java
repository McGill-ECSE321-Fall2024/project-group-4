package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Promotion;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link ca.mcgill.ecse321.gameshop.model.Promotion}
 */
public record PromotionDTO(int id, int discount, Date startDate, Date endDate) implements Serializable {
    public PromotionDTO(Promotion promotion) {
        this(promotion.getId(), promotion.getDiscount(), promotion.getStartDate(), promotion.getEndDate());
    }
}