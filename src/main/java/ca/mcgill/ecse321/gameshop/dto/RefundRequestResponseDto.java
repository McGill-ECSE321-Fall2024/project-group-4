package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.RefundRequest;
import ca.mcgill.ecse321.gameshop.model.RequestStatus;

import java.io.Serializable;

/**
 * DTO for {@link ca.mcgill.ecse321.gameshop.model.RefundRequest}
 */
public record RefundRequestResponseDto(int id, PurchaseDTO purchase, RequestStatus status,
                                       String reason) implements Serializable {

    public RefundRequestResponseDto(RefundRequest refundRequest){
        this(refundRequest.getId(), new PurchaseDTO(refundRequest.getPurchase()), refundRequest.getStatus(), refundRequest.getReason());
    }
}