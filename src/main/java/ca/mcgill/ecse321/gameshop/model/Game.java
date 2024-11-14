package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.util.HashSet;
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
    private boolean isActive;
    private int stock;
    
    @ManyToMany(mappedBy = "wishlist")
    private Set<Customer> wishlistedBy = new HashSet<>();

    @ManyToMany(mappedBy = "inCategory")
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(mappedBy = "games")
    private Set<Promotion> promotions = new HashSet<>();

    public Game(String name, String description, String coverPicture, float price, boolean isActive, int stock) {
        this.name = name;
        this.description = description;
        this.coverPicture = coverPicture;
        this.price = price;
        this.isActive = isActive;
        this.stock = stock;
    }

    protected Game() {

    }

    protected Set<Customer> getWishlistedBy() {
        return wishlistedBy;
    }

    public boolean addWishlistedBy(Customer customer){
        customer.getWishlist().add(this);
        return wishlistedBy.add(customer);
    }

    public boolean removeWishlistedBy(Customer customer){
        customer.getWishlist().remove(this);
        return wishlistedBy.remove(customer);
    }

    public boolean containsWishlistedBy(Customer customer){
        return wishlistedBy.contains(customer);
    }

    public Set<Customer> getCopyWishlistedBy(){
        return new HashSet<>(wishlistedBy);
    }
    

    protected Set<Category> getCategories() {
        return categories;
    }
    public boolean addCategory(Category category){
        category.getInCategory().add(this);
        return categories.add(category);
    }

    public boolean removeCategory(Category category){
        category.getInCategory().remove(this);
        return categories.remove(category);
    }

    public boolean containsCategory(Category category){
        return categories.contains(category);
    }

    public Set<Category> getCopyCategories(){
        return new HashSet<>(categories);
    }

    public Set<Promotion> getCopyPromotions() {
        return new HashSet<>(promotions);
    }

    public boolean addPromotion(Promotion promotion){
        promotion.getGames().add(this);
        return promotions.add(promotion);
    }

    public boolean removePromotion(Promotion promotion){
        promotion.getGames().remove(this);
        return promotions.remove(promotion);
    }

    public boolean containsPromotion(Promotion promotion){
        return promotions.contains(promotion);
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public int getStock() {
        return stock;
    }


    private boolean isAvailable;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setStock(int stock) {
        this.stock = stock;
        this.isAvailable = stock > 0;
    }
}