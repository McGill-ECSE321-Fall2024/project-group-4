package ca.mcgill.ecse321.gameshop.persistence;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Customer extends Account {
    private String email;
    private String phoneNumber;
    @ManyToMany
    @JoinTable(
            name = "wishlist_map",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> wishlist;
    @ManyToMany
    @JoinTable(
            name = "cart_map",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> cart;
    @OneToMany(mappedBy = "customer")
    private Set<Address> addresses;
    @OneToMany(mappedBy = "customer")
    private Set<CreditCard> creditCards;
    @ManyToMany(mappedBy = "likedBy")
    private Set<Review> likedReviews;
    @OneToMany(mappedBy = "customer")
    private Set<Purchase> purchases;



    public Customer(String username, String password, String email, String phoneNumber) {
        super(username, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {

    }

    public Set<CreditCard> getCreditCards() {
        return creditCards;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Set<Game> getWishlist() {
        return wishlist;
    }

    public Set<Game> getCart() {
        return cart;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
