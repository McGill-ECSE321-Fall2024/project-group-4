package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

@Entity
public class RefundRequest {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    @OneToOne(optional = false)
    private Purchase purchase;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @Column(nullable = false)
    private String reason;
    @ManyToOne
    private Employee reviewer;

    public RefundRequest(Purchase purchase, RequestStatus status, String reason, Employee reviewer) {
        this.purchase = purchase;
        this.status = status;
        this.reason = reason;
        this.reviewer = reviewer;

        purchase.setRefundRequest(this);
    }

    protected RefundRequest() {

    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Integer getId() { return id; }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Employee getReviewer() {
        return reviewer;
    }

    public boolean setReviewer(Employee reviewer) {
        if(this.reviewer != null){
            this.reviewer.removeRefundRequest(this);
        }
        this.reviewer = reviewer;
        return reviewer.getRefundRequests().add(this);
    }
}
