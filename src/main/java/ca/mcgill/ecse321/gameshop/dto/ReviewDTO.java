package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Review;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record ReviewDTO(int id,
                        int rating,
                        String text,
                        ReplyDTO reply,
                        PurchaseDTO purchase,
                        Set<CustomerDTO> likedBy) implements Serializable {

    public ReviewDTO(Review review) {
        this(review.getId(),
                review.getRating(),
                review.getText(),
                new ReplyDTO(review.getReply()),
                new PurchaseDTO(review.getPurchase()),
                review.getCopyLikedBy().stream().map(CustomerDTO::new).collect(Collectors.toSet()));
    }


}
