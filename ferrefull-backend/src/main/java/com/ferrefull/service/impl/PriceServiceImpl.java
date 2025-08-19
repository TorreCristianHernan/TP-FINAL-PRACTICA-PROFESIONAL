package com.ferrefull.service.impl;

import com.ferrefull.entity.Price;
import com.ferrefull.repository.PriceRepository;
import com.ferrefull.service.PriceService;
import com.ferrefull.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public List<Price> listPrices() {
        return priceRepository.findAll();
    }

    @Override
    public Optional<Price> getPrice(Long id) {
        return priceRepository.findById(id);
    }

    @Override
    public Price savePrice(Price price) {
        price.setState(Constant.State.ACTIVE.name());
        return priceRepository.save(price);
    }

    @Override
    public Price updatePrice(Price price) {
        if (getPrice(price.getId()).isPresent()){
            return priceRepository.save(price);
        }
        return null;
    }

    @Override
    public Price deletePrice(Long id) {
        Optional<Price> priceDB = getPrice(id);
        if (priceDB.isPresent()){
            Price priceDelete = priceDB.get();
            priceDelete.setState(Constant.State.DELETE.name());
            return priceRepository.save(priceDelete);
        }
        return null;
    }
}
