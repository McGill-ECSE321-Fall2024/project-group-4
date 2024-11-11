package ca.mcgill.ecse321.gameshop.dto;

import ca.mcgill.ecse321.gameshop.model.Account;

import java.io.Serializable;

public record AccountDTO(int id, String username, String password) implements Serializable {
    public AccountDTO(Account account) {
        this(account.getId(), account.getUsername(), account.getPassword());
    }
}
