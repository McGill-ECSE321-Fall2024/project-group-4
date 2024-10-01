package ca.mcgill.ecse321.gameshop.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Employee extends Account {
    private boolean isActive;
    @OneToMany(mappedBy = "requestor")
    private Set<GameRequest> gameRequests;
    @OneToMany(mappedBy = "reviewer")
    private Set<RefundRequest> refundRequests;

    public Employee(String username, String password, boolean isActive) {
        super(username, password);
        this.isActive = isActive;
    }

    public Employee() {

    }

    public Set<GameRequest> getGameRequests() {
        return gameRequests;
    }

    public Set<RefundRequest> getRefundRequests() {
        return refundRequests;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
