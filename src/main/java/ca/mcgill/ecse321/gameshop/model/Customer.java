package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer extends Account {
    private String email;
    private String phoneNumber;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "wishlist_map",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> wishlist = new HashSet<>();
    @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Address> addresses = new HashSet<>();
    @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<CreditCard> creditCards = new HashSet<>();
    @ManyToMany(mappedBy = "likedBy")
    private Set<Review> likedReviews = new HashSet<>();
    @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Purchase> purchases = new HashSet<>();



    public Customer(String username, String password, String email, String phoneNumber) {
        super(username, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    protected Customer() {

    }

    protected Set<Review> getLikedReviews() {
        return likedReviews;
    }

    public boolean addLikedReview(Review review){
        review.getLikedBy().add(this);
        return likedReviews.add(review);
    }

    public boolean removeLikedReview(Review review){
        review.getLikedBy().remove(this);
        return likedReviews.remove(review);
    }

    public boolean containsLikedReview(Review review){
        return likedReviews.contains(review);
    }

    public Set<Review> getCopyLikedReviews(){
        return new HashSet<>(likedReviews);
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public boolean addPurchases(Purchase purchase){
        return purchase.setCustomer(this);
    }

    public boolean removePurchases(Purchase purchase){
        return purchases.remove(purchase);
    }

    public boolean containsPurchases(Purchase purchase){
        return purchases.contains(purchase);
    }

    public Set<Purchase> getCopyPurchases(){
        return new HashSet<>(purchases);
    }

    public Set<CreditCard> getCopyofCreditCards() {
        return new HashSet<>(creditCards);
    }

    public boolean removeCreditCartFromWallet(CreditCard creditCard) {
        return creditCards.remove(creditCard);
    }

    protected Set<Address> getAddresses() {
        return addresses;
    }
    public Set<Address> getCopyAddresses() {
        return new HashSet<>(addresses);
    }

    public boolean addAddress(Address address) {return addresses.add(address);}

    public boolean removeAddress(Address address) {return addresses.remove(address);}

    public boolean containsAddress(Address address) {return addresses.contains(address);}

    protected Set<Game> getWishlist() {
        return wishlist;
    }
    public boolean addGameToWishlist(Game game){
        game.getWishlistedBy().add(this);
        return wishlist.add(game);
    }

    public boolean removeGameFromWishlist(Game game){
        game.getWishlistedBy().remove(this);
        return wishlist.remove(game);
    }

    public boolean addCreditCardToWallet(CreditCard creditCard){
        creditCard.setCustomer(this);
        return creditCards.add(creditCard);
    }

    public boolean containsGameInWishlist(Game game){
        return wishlist.contains(game);
    }

    public Set<Game> getCopyWishlist(){
        return new HashSet<>(wishlist);
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
