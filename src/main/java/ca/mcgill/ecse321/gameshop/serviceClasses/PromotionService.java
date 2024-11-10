package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public Promotion findPromotionById(int id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isPresent()) {
            return promotion.get();
        }
        throw new IllegalArgumentException("Promotion not found");
    }

    @Transactional
    public Promotion createPromotion(int discount, Date startDate, Date endDate) {
        Promotion promotion = new Promotion(discount);

        if(discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        if(endDate.before(Date.valueOf(LocalDate.now()))) {
            throw new IllegalArgumentException("End date cannot be before current date");
        }
        if(startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);

        promotionRepository.save(promotion);
        return promotion;

    }

    @Transactional
    public void deletePromotion(int id) {

        Promotion promotion = findPromotionById(id);

        Set<Game> gamesInPromotion = promotion.getCopyGames();

        gamesInPromotion.forEach(game -> {
            promotion.removeGame(game);
            game.removePromotion(promotion);
            gameRepository.save(game);
        });

        promotionRepository.delete(promotion);
    }

    @Transactional
    public Promotion updatePromotion(int id, int discount, Date startDate, Date endDate) {
        Promotion promotion = findPromotionById(id);

        if(discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        promotion.setDiscount(discount);
        if(endDate.before(Date.valueOf(LocalDate.now()))) {
            throw new IllegalArgumentException("End date cannot be before current date");
        }
        if(startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotionRepository.save(promotion);
        return promotion;

    }
}
