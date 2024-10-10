package ca.mcgill.ecse321.gameshop.persistence;

import ca.mcgill.ecse321.gameshop.persistence.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@Entity
public class Promotion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(nullable = false)
    private String discount;

    @Column @Setter @Getter
    private Date startDate;

    @Column @Setter @Getter
    private Date endDate;

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
