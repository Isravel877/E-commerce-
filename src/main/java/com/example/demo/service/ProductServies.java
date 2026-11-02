package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.products;
import com.example.demo.repo.ProductRepo;



@Service
public class ProductServies {
	
	final ProductRepo db;

	ProductServies(ProductRepo db) {
		this.db = db;
	}

	public List<products> getProducts() {
		
		return  db.findAll();
	}

	public products getProduct(int id) {
	
		return db.findById(id).orElse(null);
	}

}
