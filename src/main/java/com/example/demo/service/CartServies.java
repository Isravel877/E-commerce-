package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CartItemRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.dto.CartResponse.CartLineResponse;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.products;
import com.example.demo.repo.CartItemRepo;
import com.example.demo.repo.CartRepo;
import com.example.demo.repo.ProductRepo;

@Service
public class CartServies {
    private final CartRepo cartRepo;
    private final CartItemRepo itemRepo;
    private final ProductRepo productRepo;

    public CartServies(CartRepo cartRepo, CartItemRepo itemRepo, ProductRepo productRepo) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public CartResponse getCart(String key) {
        return toResponse(findOrCreateCart(key));
    }

    @Transactional
    public CartResponse addItem(String key, CartItemRequest request) {
        if (request == null || request.productId() == null || request.quantity() == null || request.quantity() < 1) {
            throw new IllegalArgumentException("Choose a product and a quantity of at least one.");
        }
        Cart cart = findOrCreateCart(key);
        products product = productRepo.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        CartItem item = itemRepo.findByCart_IdAndProduct_Id(cart.getId(), product.getId()).orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(0);
            return newItem;
        });
        int newQuantity = item.getQuantity() + request.quantity();
        if (newQuantity > product.getStock()) throw new IllegalArgumentException("Only " + product.getStock() + " item(s) are in stock.");
        item.setQuantity(newQuantity);
        itemRepo.save(item);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse updateItem(String key, Long itemId, Integer quantity) {
        if (quantity == null || quantity < 1) throw new IllegalArgumentException("Quantity must be at least one.");
        Cart cart = findOrCreateCart(key);
        CartItem item = itemRepo.findByIdAndCart_Id(itemId, cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found."));
        if (quantity > item.getProduct().getStock()) throw new IllegalArgumentException("Only " + item.getProduct().getStock() + " item(s) are in stock.");
        item.setQuantity(quantity);
        itemRepo.save(item);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(String key, Long itemId) {
        Cart cart = findOrCreateCart(key);
        CartItem item = itemRepo.findByIdAndCart_Id(itemId, cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found."));
        itemRepo.delete(item);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse clear(String key) {
        Cart cart = findOrCreateCart(key);
        itemRepo.deleteAll(itemRepo.findByCart_Id(cart.getId()));
        return toResponse(cart);
    }

    private Cart findOrCreateCart(String key) {
        if (key == null || key.isBlank() || key.length() > 36) throw new IllegalArgumentException("Invalid cart key.");
        return cartRepo.findByCartKey(key).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setCartKey(key);
            return cartRepo.save(cart);
        });
    }

    private CartResponse toResponse(Cart cart) {
        List<CartLineResponse> lines = itemRepo.findByCart_Id(cart.getId()).stream().map(item -> {
            products p = item.getProduct();
            int price = p.getPrice();
            return new CartLineResponse(item.getId(), p.getId(), p.getName(), p.getImageUrl(), price,
                    item.getQuantity(), (long) price * item.getQuantity(), p.getStock());
        }).toList();
        int count = lines.stream().mapToInt(CartLineResponse::quantity).sum();
        long total = lines.stream().mapToLong(CartLineResponse::subtotal).sum();
        return new CartResponse(cart.getCartKey(), lines, count, total);
    }
}
