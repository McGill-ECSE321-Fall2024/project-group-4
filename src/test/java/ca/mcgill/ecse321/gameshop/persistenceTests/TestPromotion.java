package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.GameRepository;
import ca.mcgill.ecse321.gameshop.DAO.PromotionRepository;
import ca.mcgill.ecse321.gameshop.model.Game;
import ca.mcgill.ecse321.gameshop.model.Promotion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Camille Pouliot
 */
@SpringBootTest
public class TestPromotion {
    @Autowired
    private PromotionRepository promotionRepository;

    private Promotion promotion;
    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() {
        promotion = new Promotion();
        promotion.setDiscount("20%");
        Date startDate = new Date(2024, 10, 1);
        promotion.setStartDate(startDate);
        Date endDate = new Date(2024, 10, 31);
        promotion.setEndDate(endDate);
    }

    @AfterEach
    public void tearDown() {
        promotionRepository.deleteAll();
        gameRepository.deleteAll();}

    @Test
    /**
     * Author Camille Pouliot
     * Description: Tests saving and loading a Promotion object from the database
     */
    public void testSaveAndLoadPromotion() {
        //save
        promotionRepository.save(promotion);

        //load
        Optional<Promotion> loadedPromotionOpt = promotionRepository.findById(promotion.getId());

        //compare
        assertTrue(loadedPromotionOpt.isPresent());
        Promotion loadedPromotion = loadedPromotionOpt.get();
        assertEquals(promotion.getId(), loadedPromotion.getId());
        assertEquals(promotion.getDiscount(), loadedPromotion.getDiscount());
        assertEquals(promotion.getStartDate(), loadedPromotion.getStartDate());
        assertEquals(promotion.getEndDate(), loadedPromotion.getEndDate());
    }

    @Test
    /**
     * Author: Camille Pouliot
     * Description: Test updating a Promotion object from the database
     */
    public void testUpdatePromotion() {
        //update
        promotion.setDiscount("50%");
        promotionRepository.save(promotion);

        //load
        Optional<Promotion> loadedPromotionOpt = promotionRepository.findById(promotion.getId());

        //Compare
        assertTrue(loadedPromotionOpt.isPresent());
        Promotion loadedPromotion = loadedPromotionOpt.get();
        assertEquals(promotion.getId(), loadedPromotion.getId());
        assertEquals(promotion.getDiscount(), loadedPromotion.getDiscount());
    }
    @Test
    /**
     * Author: Camille Pouliot
     * Description: Test deleting a Promotion object from the database
     */
    public void testDeletePromotion() {
        //delete
        promotionRepository.deleteById(promotion.getId());

        //compare
        assertNotNull(promotion);
        assertEquals(0, promotionRepository.count());
    }

    @Test
    /**
     * Author : Tarek Namani
     * Description : Tests saving and loading the games in promotion from databases
     */
    public void testAddGamesToPromotion() {
        //add game to promotion
        Game game = new Game();
        game.setName("testgame");
        game.setDescription("testDescription");
        game.setCoverPicture("testCover");
        game.setPrice(50.00F);
        game.setStock(100);
        game.setActive(true);

        Set<Game> gamesInPromotion = new HashSet<>();
        gamesInPromotion.add(game);
        promotion.setGames(gamesInPromotion);

        //save
        gameRepository.save(game);
        promotionRepository.save(promotion);

        //load
        Promotion promotionFromDb = promotionRepository.findById(promotion.getId()).get();

        //Assert
        assertFalse(promotionFromDb.getGames().isEmpty());
        List<Game> gamesInPromotionFromDb = new ArrayList<>(promotionFromDb.getGames());
        assertEquals(game.getName(),gamesInPromotionFromDb.get(0).getName());




    }


}
