package com.example.demo.controler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.products;
import com.example.demo.service.ProductServies;

@RestController
@CrossOrigin(origins = "${app.frontend-url:http://localhost:5173}")
public class ProductControler {
    private final ProductServies product;
    public ProductControler(ProductServies product) { this.product = product; }

    @GetMapping("/products")
    public List<products> getProducts() { return product.getProducts(); }

    @GetMapping("/products/{id}")
    public products getProduct(@PathVariable int id) {
        return product.getProduct(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}
