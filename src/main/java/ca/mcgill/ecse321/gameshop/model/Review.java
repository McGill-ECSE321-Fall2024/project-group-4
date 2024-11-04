package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int rating;
    @Column(nullable = false)
    private String text;
    @OneToOne(mappedBy = "review", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Reply reply;
    @OneToOne(optional = false)
    private Purchase purchase;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "like_map",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> likedBy = new HashSet<>();

    public Review(int rating, String text, Purchase purchase) {
        this.rating = rating;
        this.text = text;
        this.purchase = purchase;

        purchase.setReview(this);
    }

    protected Review() {

    }

    public int getId() {
        return id;
    }

    protected Set<Customer> getLikedBy() {
        return likedBy;
    }

    public boolean addLikedBy(Customer customer){
        customer.getLikedReviews().add(this);
        return likedBy.add(customer);
    }

    public boolean removeLikedBy(Customer customer){
        customer.getLikedReviews().remove(this);
        return likedBy.remove(customer);
    }

    public boolean containsLikedBy(Customer customer){
        return likedBy.contains(customer);
    }

    public Set<Customer> getCopyLikedBy(){
        return new HashSet<>(likedBy);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
        if(reply != null && reply.getReview().getId() != getId()){
            reply.setReview(this);
        }
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase.setReview(null);
        this.purchase = purchase;
        purchase.setReview(this);
    }
}
