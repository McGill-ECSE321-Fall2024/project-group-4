package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.HashSet;
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

    @ManyToMany
    @JoinTable(
            name = "promotion_map",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> games = new HashSet<>();

    public Promotion(String discount) {
        this.discount = discount;
    }

    protected Promotion() {

    }

    public int getId() {
        return id;
    }

    public String getDiscount() {
        return discount;
    }

    protected Set<Game> getGames() {
        return games;
    }
    public boolean addGame(Game game){
        game.getCopyPromotions().add(this);
        return games.add(game);
    }

    public boolean removeGame(Game game){
        game.getCopyPromotions().remove(this);
        return games.remove(game);
    }

    public boolean containsGame(Game game){
        return games.contains(game);
    }

    public Set<Game> getCopyGames(){
        return new HashSet<>(games);
    }
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setStartDate(Date startDate) {this.startDate = startDate;}

    public Date getStartDate() {return startDate;}

    public void setEndDate(Date sndDate) {this.endDate = endDate;}

    public Date getEndDate() {return endDate;}
}
