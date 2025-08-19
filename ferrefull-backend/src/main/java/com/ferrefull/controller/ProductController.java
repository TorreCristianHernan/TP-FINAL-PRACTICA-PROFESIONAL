package com.ferrefull.controller;

import com.ferrefull.entity.Product;
import com.ferrefull.service.ProductService;
import com.ferrefull.util.Util;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/product")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProducts(){
        return ResponseEntity.ok(productService.listProducts());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        return productService.getProduct(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody Product product, BindingResult result){
        if (result.hasErrors()){
            log.error("ERROR VALIDANDO DATOS");
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        product.setId(id);
        Product productDB = productService.updateProduct(product);
        if (Objects.isNull(productDB)){
            log.error("NO RETORNO CLIENTE");
            return ResponseEntity.notFound().build();
        }
      log.error("RETORNO TODO OK");
        return ResponseEntity.ok(productDB);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id){
        Product productDB = productService.deleteProduct(id);
        if (Objects.isNull(productDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDB);
    }

}
