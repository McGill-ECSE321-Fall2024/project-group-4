package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.RefundRequest;
import ca.mcgill.ecse321.gameshop.model.RequestStatus;

import java.io.Serializable;

public record RefundRequestDTO(
        int id, PurchaseDTO purchcase, RequestStatus status, String reason, EmployeeDTO reviewer
) implements Serializable {

    public RefundRequestDTO(RefundRequest refundRequest) {
        this(refundRequest.getId(), new PurchaseDTO(refundRequest.getPurchase()) ,refundRequest.getStatus(), refundRequest.getReason(), new EmployeeDTO(refundRequest.getReviewer()));
    }
}