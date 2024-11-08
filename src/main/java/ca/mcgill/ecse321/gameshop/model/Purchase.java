package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class Purchase {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private LocalDate purchaseDate;
    private float purchasePrice;
    @ManyToOne(optional = false)
    private Game gamePurchased;
    @ManyToOne(optional = false)
    private Customer customer;
    @ManyToOne(optional = false)
    private Address deliveryAddress;
    @ManyToOne(optional = false)
    private CreditCard paymentMethod;
    @OneToOne(mappedBy = "purchase", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Review review;
    @OneToOne(mappedBy = "purchase", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private RefundRequest refundRequest;

    @PrePersist
    private void onCreate(){
        this.purchaseDate = LocalDate.now();
    }

    public Purchase(LocalDate purchaseDate, float purchasePrice, Game gamePurchased, Customer customer, Address deliveryAddress, CreditCard paymentMethod) {
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.gamePurchased = gamePurchased;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;

        customer.getPurchases().add(this);
    }
    protected Purchase() {

    }

    public RefundRequest getRefundRequest() {
        return refundRequest;
    }

    public void setRefundRequest(RefundRequest refundRequest) {
        this.refundRequest = refundRequest;
    }

    public int getId() {
        return id;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Game getGamePurchased() {
        return gamePurchased;
    }

    public void setGamePurchased(Game gamePurchased) {
        this.gamePurchased = gamePurchased;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean setCustomer(Customer customer) {
        this.customer.getPurchases().remove(this);
        this.customer = customer;
        return customer.getPurchases().add(this);
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public CreditCard getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(CreditCard paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
        if(review != null && review.getPurchase().getId() != getId()){
            review.setPurchase(this);
        }
    }
}
