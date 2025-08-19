package com.ferrefull.service;

import com.ferrefull.entity.Price;

import java.util.List;
import java.util.Optional;

public interface PriceService {
    public List<Price> listPrices();
    public Optional<Price> getPrice(Long id);
    public Price savePrice(Price price);
    public Price updatePrice(Price price);
    public Price deletePrice(Long id);
}
