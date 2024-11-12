package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Policy;

import java.io.Serializable;

public record PolicyDTO(int id, String description) implements Serializable {
    public PolicyDTO(Policy policy){
        this(policy.getId(), policy.getDescription());
    }
}
