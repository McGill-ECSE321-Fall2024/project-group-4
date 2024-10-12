package ca.mcgill.ecse321.gameshop.persistenceTests;

import ca.mcgill.ecse321.gameshop.DAO.ReplyRepository;
import ca.mcgill.ecse321.gameshop.DAO.ReviewRepository;
import ca.mcgill.ecse321.gameshop.persistence.Reply;
import ca.mcgill.ecse321.gameshop.persistence.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Author: Camille Pouliot
 */
@SpringBootTest
public class TestReply {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    
}
