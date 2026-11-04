package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Cart;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartKey(String cartKey);
}
