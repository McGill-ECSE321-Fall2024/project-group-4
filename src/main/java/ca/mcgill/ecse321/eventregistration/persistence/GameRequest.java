package ca.mcgill.ecse321.eventregistration.persistence;

import jakarta.persistence.*;

@Entity
public class GameRequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String externalReview;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @ManyToOne(optional = false)
    private Employee requestor;
    @OneToOne(optional = false)
    private Game game;

    public GameRequest(String externalReview, RequestStatus status, Employee requestor, Game game) {
        this.externalReview = externalReview;
        this.status = status;
        this.requestor = requestor;
        this.game = game;
    }

    public GameRequest() {

    }

    public int getId() {
        return id;
    }

    public Employee getRequestor() {
        return requestor;
    }

    public void setRequestor(Employee requestor) {
        this.requestor = requestor;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getExternalReview() {
        return externalReview;
    }

    public void setExternalReview(String externalReview) {
        this.externalReview = externalReview;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
