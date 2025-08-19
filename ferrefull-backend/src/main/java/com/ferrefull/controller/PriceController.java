package com.ferrefull.controller;

import com.ferrefull.entity.Price;
import com.ferrefull.service.PriceService;
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
@RequestMapping(value = "/api/price")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping
    public ResponseEntity<List<Price>> listPrices(){
        return ResponseEntity.ok(priceService.listPrices());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Price> getPrice(@PathVariable("id") Long id){
        return priceService.getPrice(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Price> addPrice(@Valid @RequestBody Price prices, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.savePrice(prices));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Price> updatePrice(@PathVariable("id") Long id, @Valid @RequestBody Price price, BindingResult result){
        if (result.hasErrors()){
            log.error("ERROR VALIDANDO DATOS");
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        price.setId(id);
        Price priceDB = priceService.updatePrice(price);
        if (Objects.isNull(priceDB)){
            log.error("NO RETORNO CLIENTE");
            return ResponseEntity.notFound().build();
        }
      log.error("RETORNO TODO OK");
        return ResponseEntity.ok(priceDB);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Price> updatePrice(@PathVariable("id") Long id){
        Price priceDB = priceService.deletePrice(id);
        if (Objects.isNull(priceDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(priceDB);
    }

}
