package com.ferrefull.service;

import com.ferrefull.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public List<Product> listProducts();
    public Optional<Product> getProduct(Long id);
    public Product saveProduct(Product product);
    public Product updateProduct(Product product);
    public Product deleteProduct(Long id);
}
