package ca.mcgill.ecse321.gameshop.persistence;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int rating;
    @Column(nullable = false)
    private String text;
    @OneToOne(mappedBy = "review")
    private Reply reply;
    @OneToOne(optional = false)
    private Purchase purchase;
    @ManyToMany
    @JoinTable(
            name = "like_map",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> likedBy;

    public Review(int rating, String text, Purchase purchase) {
        this.rating = rating;
        this.text = text;
        this.purchase = purchase;
    }

    public Review() {

    }

    public int getId() {
        return id;
    }

    public Set<Customer> getLikedBy() {
        return likedBy;
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
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}
