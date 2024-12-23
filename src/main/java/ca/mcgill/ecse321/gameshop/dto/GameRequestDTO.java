package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.GameRequest;
import ca.mcgill.ecse321.gameshop.model.RequestStatus;

import java.io.Serializable;

public record GameRequestDTO(
        int id,
        String externalReview,
        RequestStatus requestStatus,
        EmployeeResponseDTO requestor,
        GameResponseDTO game

) implements Serializable {
    public GameRequestDTO(GameRequest gameRequest) {
        this(gameRequest.getId(), gameRequest.getExternalReview(), gameRequest.getStatus(), new EmployeeResponseDTO(gameRequest.getRequestor()), new GameResponseDTO(gameRequest.getGame()));
    }
}