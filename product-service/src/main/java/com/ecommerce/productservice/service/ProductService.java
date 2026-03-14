package com.ecommerce.productservice.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.productservice.entity.Product;

public interface ProductService {

    Product createProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> findById(Long id);
}
