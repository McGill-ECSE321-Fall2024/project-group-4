package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.RefundRequest;
import ca.mcgill.ecse321.gameshop.model.RequestStatus;

import java.io.Serializable;

public record RefundRequestDTO(
        int id, RequestStatus status, String reason, EmployeeResponseDTO reviewer
) implements Serializable {

    public RefundRequestDTO(RefundRequest refundRequest) {
        this(refundRequest.getId() ,refundRequest.getStatus(), refundRequest.getReason(), new EmployeeResponseDTO(refundRequest.getReviewer()));
    }
}