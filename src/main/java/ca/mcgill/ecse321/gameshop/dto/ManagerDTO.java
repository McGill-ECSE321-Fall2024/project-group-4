package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Manager;

public record ManagerDTO(int id, String username, String password) {
    public ManagerDTO(Manager manager) {
        this(manager.getId(), manager.getUsername(), manager.getPassword());
    }
}
