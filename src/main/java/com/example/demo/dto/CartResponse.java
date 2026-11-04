package com.example.demo.dto;

import java.util.List;

public record CartResponse(String cartKey, List<CartLineResponse> items, int itemCount, long total) {
    public record CartLineResponse(Long id, Integer productId, String name, String imageUrl,
                                   int unitPrice, int quantity, long subtotal, int stock) { }
}
