package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Reply;

import java.io.Serializable;

public record ReplyDTO(int id, String text, ReviewDTO review) implements Serializable {
    public ReplyDTO (Reply reply) {
        this(reply.getId(), reply.getText(), new ReviewDTO(reply.getReview()));
    }
}
