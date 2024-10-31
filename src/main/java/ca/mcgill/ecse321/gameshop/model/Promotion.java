package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Set;

@Entity
public class Promotion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(nullable = false)
    private String discount;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @ManyToMany(fetch = FetchType.EAGER)
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

    public void setGames(Set<Game> games) {this.games = games;}

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setStartDate(Date startDate) {this.startDate = startDate;}

    public Date getStartDate() {return startDate;}

    public void setEndDate(Date sndDate) {this.endDate = endDate;}

    public Date getEndDate() {return endDate;}
}
