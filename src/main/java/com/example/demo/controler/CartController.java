package com.example.demo.controler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartItemRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.service.CartServies;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartServies cartService;

    public CartController(CartServies cartService) { this.cartService = cartService; }

    @GetMapping("/{cartKey}")
    public CartResponse getCart(@PathVariable String cartKey) { return cartService.getCart(cartKey); }

    @PostMapping("/{cartKey}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(@PathVariable String cartKey, @RequestBody CartItemRequest request) {
        return cartService.addItem(cartKey, request);
    }

    @PutMapping("/{cartKey}/items/{itemId}")
    public CartResponse updateItem(@PathVariable String cartKey, @PathVariable Long itemId, @RequestParam Integer quantity) {
        return cartService.updateItem(cartKey, itemId, quantity);
    }

    @DeleteMapping("/{cartKey}/items/{itemId}")
    public CartResponse removeItem(@PathVariable String cartKey, @PathVariable Long itemId) {
        return cartService.removeItem(cartKey, itemId);
    }

    @DeleteMapping("/{cartKey}/items")
    public CartResponse clear(@PathVariable String cartKey) { return cartService.clear(cartKey); }
}
