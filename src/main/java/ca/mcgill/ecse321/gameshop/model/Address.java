package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

@Entity
public class Address {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String province;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String postalCode;
    @ManyToOne(optional = false)
    private Customer customer;

    public Address(String street, String city, String province, String country, String postalCode, Customer customer) {
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
        this.customer = customer;

        customer.getAddresses().add(this);
    }

    protected Address() {

    }

    public Customer getCustomer() {
        return customer;
    }

    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
