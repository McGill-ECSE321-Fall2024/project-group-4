package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CreditCard {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int cardNumber;
    private int cvv;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @ManyToOne(optional = false)
    private Customer customer;
    @ManyToOne(optional = false)
    private Address billingAddress;

    public CreditCard(int cardNumber, int cvv, LocalDate expiryDate, Customer customer, Address billingAddress) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.customer = customer;
        this.billingAddress = billingAddress;
    }

    public CreditCard() {

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

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
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
