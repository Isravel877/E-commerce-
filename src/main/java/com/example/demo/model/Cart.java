package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // A random browser-generated key lets guests keep a server-stored cart.
    @Column(unique = true, length = 36)
    private String cartKey;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCartKey() { return cartKey; }
    public void setCartKey(String cartKey) { this.cartKey = cartKey; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
