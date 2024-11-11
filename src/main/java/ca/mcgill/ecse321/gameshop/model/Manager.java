package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.Entity;

@Entity
public class Manager extends Account {
    Manager(String username, String password) {
        super(username, password);
    }


    public Manager() {
        super();
    }
}
