package ca.mcgill.ecse321.gameshop.persistence;

import ca.mcgill.ecse321.gameshop.persistence.Game;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Promotion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(nullable = false)
    private String discount;

    @ManyToMany
    @JoinTable(
            name = "promotion_map",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> games;

    public Promotion(String discount) {
        this.discount = discount;
    }

    public Promotion() {

    }

    public int getId() {
        return id;
    }

    public String getDiscount() {
        return discount;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
