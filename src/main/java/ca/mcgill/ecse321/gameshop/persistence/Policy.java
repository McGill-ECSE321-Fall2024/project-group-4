package ca.mcgill.ecse321.gameshop.persistence;

import jakarta.persistence.*;

@Entity
public class Policy {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(nullable = false)
    private String description;
}
