package com.ferrefull.service.impl;

import com.ferrefull.entity.Product;
import com.ferrefull.repository.ProductRepository;
import com.ferrefull.service.ProductService;
import com.ferrefull.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        product.setState(Constant.State.ACTIVE.name());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if (getProduct(product.getId()).isPresent()){
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public Product deleteProduct(Long id) {
        Optional<Product> productDB = getProduct(id);
        if (productDB.isPresent()){
            Product productDelete = productDB.get();
            productDelete.setState(Constant.State.DELETE.name());
            return productRepository.save(productDelete);
        }
        return null;
    }
}
