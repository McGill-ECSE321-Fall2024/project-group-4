package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

@Entity
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(nullable = false)
    private String text;
    @OneToOne(optional = false)
    private Review review;

    public Reply(String text, Review review) {
        this.text = text;
        this.review = review;
    }

    public Reply() {

    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
