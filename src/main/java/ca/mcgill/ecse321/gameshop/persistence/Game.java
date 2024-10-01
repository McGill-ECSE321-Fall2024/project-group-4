package ca.mcgill.ecse321.gameshop.persistence;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Game {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String description;
    private String coverPicture;
    private float price;
    private boolean is_active;
    private int stock;
    @ManyToMany(mappedBy = "wishlist")
    private Set<Customer> wishlistedBy;
    @ManyToMany(mappedBy = "cart")
    private Set<Customer> inCartOf;
    @ManyToMany(mappedBy = "inCategory")
    private Set<Category> categories;
    @ManyToMany(mappedBy = "games")
    private Set<Promotion> promotions;
    @OneToMany(mappedBy = "gamePurchased")
    private Set<Purchase> purchases;

    public Game(String name, String description, String coverPicture, float price, boolean is_active, int stock) {
        this.name = name;
        this.description = description;
        this.coverPicture = coverPicture;
        this.price = price;
        this.is_active = is_active;
        this.stock = stock;
    }

    public Game() {

    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public Set<Customer> getWishlistedBy() {
        return wishlistedBy;
    }

    public Set<Customer> getInCartOf() {
        return inCartOf;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<Promotion> getPromotions() {
        return promotions;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
