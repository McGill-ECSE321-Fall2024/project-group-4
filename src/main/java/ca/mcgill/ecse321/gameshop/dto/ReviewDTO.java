package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Review;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record ReviewDTO(int id,
                        int rating,
                        String text,
                        ReplyDTO reply,
                        String username,
                        Set<CustomerResponseDTO> likedBy) implements Serializable {

    public ReviewDTO(Review review) {
        this(review.getId(),
                review.getRating(),
                review.getText(),
                review.getReply() != null ? new ReplyDTO(review.getReply()) : null,
                review.getPurchase().getCustomer().getUsername(),
                review.getCopyLikedBy().stream().map(CustomerResponseDTO::new).collect(Collectors.toSet()));
    }


}