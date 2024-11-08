package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Employee extends Account {
    private boolean isActive;
    @OneToMany(mappedBy = "requestor", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<GameRequest> gameRequests = new HashSet<>();
    @OneToMany(mappedBy = "reviewer", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<RefundRequest> refundRequests = new HashSet<>();

    public Employee(String username, String password, boolean isActive) {
        super(username, password);
        this.isActive = isActive;
    }

    protected Employee() {

    }

    protected Set<GameRequest> getGameRequests() {
        return gameRequests;
    }

    public Set<GameRequest> getCopyGameRequests() {
        return new HashSet<>(gameRequests);
    }

    public boolean addGameRequest(GameRequest gameRequest){
        return gameRequest.setRequestor(this);
    }

    public boolean removeGameRequest(GameRequest gameRequest){
        return gameRequests.remove(gameRequest);
    }

    public boolean containsGameRequest(GameRequest gameRequest){
        return gameRequests.contains(gameRequest);
    }

    public Set<RefundRequest> getCopyRefundRequests() {
        return new HashSet<>(refundRequests);
    }

    public boolean addRefundRequest(RefundRequest refundRequest){
        return refundRequest.setReviewer(this);
    }

    public boolean removeRefundRequest(RefundRequest refundRequest){
        return refundRequests.remove(refundRequest);
    }

    public boolean containsRefundRequest(RefundRequest refundRequest){
        return refundRequests.contains(refundRequest);
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
