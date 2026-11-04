package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.products;
import com.example.demo.repo.ProductRepo;

@Service
public class ProductServies {
    private final ProductRepo db;
    public ProductServies(ProductRepo db) { this.db = db; }
    public List<products> getProducts() { return db.findAll(); }
    public Optional<products> getProduct(int id) { return db.findById(id); }
}
