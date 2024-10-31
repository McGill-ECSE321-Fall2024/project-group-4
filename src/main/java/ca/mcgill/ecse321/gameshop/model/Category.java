package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

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
    private Set<Game> inCategory;

    public Category(String name) {
        this.name = name;
    }

    public Category() {

    }

    public Set<Game> getInCategory() {
        return inCategory;
    }

    public void setInCategory(Set<Game> inCategory) {this.inCategory = inCategory;}

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
