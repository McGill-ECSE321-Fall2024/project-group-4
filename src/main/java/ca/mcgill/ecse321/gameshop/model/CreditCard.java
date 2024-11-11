package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CreditCard {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int cardNumber;
    private String cvv;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @ManyToOne(optional = false)
    private Customer customer;
    @ManyToOne(optional = false)
    private Address billingAddress;

    public CreditCard(int cardNumber, String cvv, LocalDate expiryDate, Customer customer, Address billingAddress) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.customer = customer;
        this.billingAddress = billingAddress;
    }

    protected CreditCard() {

    }

    public int getId() {
        return id;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }
}
