package ca.mcgill.ecse321.gameshop.serviceTests;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.*;
import ca.mcgill.ecse321.gameshop.serviceClasses.GameManagementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for promotion service
 *
 * @author Camille Pouliot
 */
@SpringBootTest
public class TestPromotionService {

    @InjectMocks
    private GameManagementService promotionService;

    @Mock
    private PromotionRepository promotionRepository;
    @Mock
    private GameRepository gameRepository;

    Game game;
    Game game2;
    Promotion promotion;
    @Autowired
    private GameManagementService gameManagementService;

    @BeforeEach
    void setUp() {
        game = new Game("Game Name", "Game description", "Game Picture", 10, true, 1);
        game2 = new Game("Other Game Name", "Other Game description", "Other Game Picture", 15, true, 1);
        promotion = new Promotion(25);
        promotion.setStartDate(Date.valueOf(LocalDate.now()));
        promotion.setEndDate(Date.valueOf(LocalDate.now().plusDays(1)));
        game.addPromotion(promotion);
        game2.addPromotion(promotion);

        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        when(promotionRepository.findById(promotion.getId())).thenReturn(Optional.of(promotion));
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(promotionRepository.findById(-1)).thenReturn(Optional.empty());

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(gameRepository.findById(2)).thenReturn(Optional.of(game2));
    }

    @AfterEach
    void tearDown() {
        promotionRepository.deleteAll();
        gameRepository.deleteAll();
    }

    /**
     * Test finding a promotion
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindPromotionById() {
        //Act
        Promotion loadedPromotion = promotionService.findPromotionById(promotion.getId());

        //Assert
        assertNotNull(loadedPromotion);
        assertEquals(promotion.getId(), loadedPromotion.getId());
        assertEquals(promotion.getDiscount(), loadedPromotion.getDiscount());
        assertEquals(promotion.getStartDate(), loadedPromotion.getStartDate());
        assertEquals(promotion.getEndDate(), loadedPromotion.getEndDate());
        assertEquals(promotion, loadedPromotion);
        verify(promotionRepository, times(1)).findById(promotion.getId());
    }

    /**
     * Test finding a promotion with an invalid id
     *
     * @author Camille Pouliot
     */
    @Test
    public void testFindPromotionByInvalidId() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> promotionService.findPromotionById(-1));

        //Assert
        assertEquals("Promotion not found", exception.getMessage());
        verify(promotionRepository, times(1)).findById(-1);
    }

    /**
     * Test creating a promotion
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreatePromotion() {
        //Act
        Promotion createdPromotion = promotionService.createPromotion(25, Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(1)));

        //Assert
        assertNotNull(createdPromotion);
        assertEquals(promotion.getDiscount(), createdPromotion.getDiscount());
        assertEquals(promotion.getStartDate(), createdPromotion.getStartDate());
        assertEquals(promotion.getEndDate(), createdPromotion.getEndDate());
        verify(promotionRepository, times(1)).save(createdPromotion);
    }

    /**
     * Test creating a promotion with an invalid discount less than 0
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreatePromotionDiscountLT0() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.createPromotion(-1, Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(1)));});

        //Assert
        assertEquals("Discount must be between 0 and 100", exception.getMessage());
    }

    /**
     * Test creating a promotion with an invalid discount greater than 100
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreatePromotionDiscountTGT100() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.createPromotion(101, Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(1)));});

        //Assert
        assertEquals("Discount must be between 0 and 100", exception.getMessage());
    }

    /**
     * Test creating a promotion with an end date before the start date
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreatePromotionEndBeforeStart() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.createPromotion(25, Date.valueOf(LocalDate.now().plusDays(2)), Date.valueOf(LocalDate.now().plusDays(1)));});

        //Assert
        assertEquals("Start date cannot be after end date", exception.getMessage());
    }

    /**
     * Test creating a promotion with an end date before the current date
     *
     * @author Camille Pouliot
     */
    @Test
    public void testCreatePromotionEndBeforeCurrent() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.createPromotion(25, Date.valueOf(LocalDate.now().minusDays(2)), Date.valueOf(LocalDate.now().minusDays(1)));});

        //Assert
        assertEquals("End date cannot be before current date", exception.getMessage());
    }

    /**
     * Test deleting a promotion
     *
     * @author Camille Pouliot
     */
    @Test
    public void testDeletePromotion() {
        //Act
        promotionService.deletePromotion(promotion.getId());

        //Assert
        assertEquals(0, game.getCopyPromotions().size());
        assertEquals(0, game2.getCopyPromotions().size());
        verify(gameRepository, times(1)).save(game);
        verify(gameRepository, times(1)).save(game2);
        verify(promotionRepository, times(1)).delete(promotion);
    }

    /**
     * Test updating a promotion
     *
     * @author Camille Pouliot
     */
    @Test
    public void testUpdatePromotion() {
        //Act
        Promotion updatedPromotion = promotionService.updatePromotion(promotion.getId(), 20, Date.valueOf(LocalDate.now().minusDays(2)), Date.valueOf(LocalDate.now().plusDays(2)));

        //Assert
        assertNotNull(updatedPromotion);
        assertEquals(promotion.getId(), updatedPromotion.getId());
        assertEquals(20, updatedPromotion.getDiscount());
        assertEquals(Date.valueOf(LocalDate.now().minusDays(2)), updatedPromotion.getStartDate());
        assertEquals(Date.valueOf(LocalDate.now().plusDays(2)), updatedPromotion.getEndDate());
        verify(promotionRepository, times(1)).save(updatedPromotion);
    }

    /**
     * Test updating a promotion with an invalid discount less than 0
     *
     * @author Camille Pouliot
     */
    @Test
    public void testUpdatePromotionDiscountLT0() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.updatePromotion(promotion.getId(), -1, promotion.getStartDate(), promotion.getEndDate());});

        //Assert
        assertEquals("Discount must be between 0 and 100", exception.getMessage());
        verify(promotionRepository, times(1)).findById(promotion.getId());
    }

    /**
     * Test updating a promotion with an invalid discount greater than 100
     *
     * @author Camille Pouliot
     */
    @Test
    public void testUpdatePromotionDiscountTGT100() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.updatePromotion(promotion.getId(), 101, promotion.getStartDate(), promotion.getEndDate());});

        //Assert
        assertEquals("Discount must be between 0 and 100", exception.getMessage());
        verify(promotionRepository, times(1)).findById(promotion.getId());
    }

    /**
     * Test updating a promotion with an end date before the start date
     *
     * @author Camille Pouliot
     */
    @Test
    public void testUpdatePromotionEndBeforeStart() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.updatePromotion(promotion.getId(), promotion.getDiscount(), Date.valueOf(LocalDate.now().plusDays(2)), Date.valueOf(LocalDate.now().plusDays(1)));});

        //Assert
        assertEquals("Start date cannot be after end date", exception.getMessage());
        verify(promotionRepository, times(1)).findById(promotion.getId());
    }

    /**
     * Test updating a promotion with an end date before the current date
     *
     * @author Camille Pouliot
     */
    @Test
    public void testUpdatePromotionEndBeforeCurrent() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.updatePromotion(promotion.getId(), promotion.getDiscount(), Date.valueOf(LocalDate.now().minusDays(2)), Date.valueOf(LocalDate.now().minusDays(1)));});

        //Assert
        assertEquals("End date cannot be before current date", exception.getMessage());
        verify(promotionRepository, times(1)).findById(promotion.getId());
    }

    @Test
    public void testAddPromotionToGame() {
        //Act
        game.removePromotion(promotion);
        game2.removePromotion(promotion);
        promotionService.addPromotionToGame(promotion.getId(), 1);

        //Assert
        Promotion updatedPromotion = promotionService.findPromotionById(promotion.getId());
        assertNotNull(updatedPromotion);
        assertEquals(1, updatedPromotion.getCopyGames().size());
        verify(promotionRepository, times(1)).save(promotion);


        Game updatedGame = promotionService.findGameById(1);
        assertNotNull(updatedGame);
        assertEquals(1, updatedGame.getCopyPromotions().size());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void testAddPromotionToGameInvalid() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.addPromotionToGame(promotion.getId(), 1);});

        //Assert
        assertEquals("Promotion already applied to game", exception.getMessage());
        verify(promotionRepository, times(1)).findById(promotion.getId());
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    public void testRemovePromotionFromGame() {
        //Act
        promotionService.removePromotionFromGame(promotion.getId(), 1);

        //Assert
        Promotion updatedPromotion = promotionService.findPromotionById(promotion.getId());
        assertNotNull(updatedPromotion);
        assertEquals(1, updatedPromotion.getCopyGames().size());
        verify(promotionRepository, times(1)).save(promotion);

        Game updatedGame = promotionService.findGameById(1);
        assertNotNull(updatedGame);
        assertEquals(0, updatedGame.getCopyPromotions().size());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void testRemovePromotionFromGameInvalid() {
        //Act
        game.removePromotion(promotion);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {promotionService.removePromotionFromGame(promotion.getId(), 1);});

        //Assert
        assertEquals("Promotion not applied to game", exception.getMessage());
        verify(promotionRepository, times(1)).findById(promotion.getId());
        verify(gameRepository, times(1)).findById(1);
    }

}