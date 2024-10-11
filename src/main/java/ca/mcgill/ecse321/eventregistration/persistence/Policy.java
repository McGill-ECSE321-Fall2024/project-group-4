package ca.mcgill.ecse321.eventregistration.persistence;

import jakarta.persistence.*;

@Entity
public class Policy {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(nullable = false)
    private String description;

    public Policy(String description) {
        this.description = description;
    }

    public Policy() {

    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
