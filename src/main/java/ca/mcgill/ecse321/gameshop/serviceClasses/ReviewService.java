package ca.mcgill.ecse321.gameshop.serviceClasses;

import ca.mcgill.ecse321.gameshop.DAO.*;
import ca.mcgill.ecse321.gameshop.model.Customer;
import ca.mcgill.ecse321.gameshop.model.Purchase;
import ca.mcgill.ecse321.gameshop.model.Reply;
import ca.mcgill.ecse321.gameshop.model.Review;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public Review findReviewById(int id) {
        Optional<Review> optReview = reviewRepository.findById(id);
        if (optReview.isPresent()) {
            return optReview.get();
        }
        throw new IllegalArgumentException("No Review found with id " + id);
    }
    @Transactional
    public Customer findCustomerByEmail(String email) {
        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        if (optCustomer.isPresent()) {
            return optCustomer.get();
        }
        throw new IllegalArgumentException("No Customer found with email " + email);

    }
    @Transactional
    public Purchase findPurchaseById(int id) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(id);
        if (optPurchase.isPresent()) {
            return optPurchase.get();
        }
        throw new IllegalArgumentException("No Purchase found with id " + id);
    }

    @Transactional
    public Review createReview(int rating, String text, int purchaseId) {

        Purchase purchase = findPurchaseById(purchaseId);
        Review review = new Review(rating, text, purchase);
        reviewRepository.save(review);

        return review;

    }

    @Transactional
    public void likeReview(String customerEmail, int reviewId ) {

        Customer customer = findCustomerByEmail(customerEmail);
        Review review = findReviewById(reviewId);

        Set<Customer> likedBy;
        if (review.getLikedBy() == null) {
            likedBy = new HashSet<>();
        } else {
           likedBy = review.getLikedBy();
        }
        likedBy.add(customer);
        review.setLikedBy(likedBy);

        Set<Review> likes;
        if (customer.getLikedReviews() == null) {
            likes = new HashSet<>();
        } else {
            likes = customer.getLikedReviews();
        }
        likes.add(review);
        customer.setLikedReviews(likes);

        customerRepository.save(customer);
        reviewRepository.save(review);



    }

    @Transactional
    public Review postReview(int rating, String text, int purchaseId) {
        if (text == null) {
            throw new IllegalArgumentException("Review text is null");
        }
        if (rating > 5 || rating < 0) {
            throw new IllegalArgumentException("Review rating is out of range");
        }
        Purchase purchase = findPurchaseById(purchaseId);
        Review review = createReview(rating, text, purchaseId);
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public void replyToReview(int reviewId, String replyText) {

        if (replyText == null) throw new IllegalArgumentException("reply text is null");

        Review review = findReviewById(reviewId);
        Reply reply = new Reply(replyText,review);
        review.setReply(reply);

        replyRepository.save(reply);
        reviewRepository.save(review);


    }


}
