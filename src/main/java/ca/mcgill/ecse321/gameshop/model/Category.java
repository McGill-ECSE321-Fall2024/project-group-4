package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "category_map",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> inCategory = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    protected Category() {

    }

    protected Set<Game> getInCategory() {
        return inCategory;
    }

    public boolean addInCategory(Game game){
        game.getCategories().add(this);
        return inCategory.add(game);
    }

    public boolean removeInCategory(Game game){
        game.getCategories().remove(this);
        return inCategory.remove(game);
    }

    public boolean containsInCategory(Game game){
        return inCategory.contains(game);
    }

    public Set<Game> getCopyInCategory(){
        return new HashSet<>(inCategory);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
