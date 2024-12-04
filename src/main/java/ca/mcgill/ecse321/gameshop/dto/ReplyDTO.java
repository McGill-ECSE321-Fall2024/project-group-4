package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Reply;

import java.io.Serializable;

public record ReplyDTO(int id, String text) implements Serializable {
    public ReplyDTO (Reply reply) {
        this(reply.getId(), reply.getText());
    }
}