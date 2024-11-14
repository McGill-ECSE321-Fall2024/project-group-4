package ca.mcgill.ecse321.gameshop.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class CartItem {
    @EmbeddedId
    private CartItemId cartItemId;
    private int quantity;


    public CartItem(int quantity, Customer customer, Game game) {
        this.quantity = quantity;
        this.cartItemId = new CartItemId(customer, game);

    }

    protected CartItem() {

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Customer getCustomer() {
        return this.cartItemId.getCustomer();
    }

    public Game getGame() {
        return this.cartItemId.getGame();
    }

    public CartItemId getCartItemId() {
        return cartItemId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CartItem that)) {
            return false;
        }
        return this.cartItemId.equals(that.cartItemId);
    }

    @Override
    public int hashCode() {
        return cartItemId.hashCode();
    }

    @Embeddable
    public static class CartItemId implements Serializable {
        @ManyToOne
        private Customer customer;
        @ManyToOne
        private Game game;

        public CartItemId(Customer customer, Game game) {
            this.customer = customer;
            this.game = game;
        }
        protected CartItemId() {

        }

        public Customer getCustomer() {
            return customer;
        }

        public Game getGame() {
            return game;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CartItemId that)) {
                return false;
            }
            return customer.getId() == that.getCustomer().getId()
                    && game.getId() == that.getGame().getId();
        }
        @Override
        public int hashCode() {
            return Objects.hash(customer.getId(), game.getId());
        }


    }
}