package com.ferrefull.service.impl;

import com.ferrefull.entity.City;
import com.ferrefull.repository.CityRepository;
import com.ferrefull.service.CityService;
import com.ferrefull.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    @Override
    public List<City> listCities() {
        return cityRepository.findAll();
    }

    @Override
    public Optional<City> getCity(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public City saveCity(City city) {
        city.setState(Constant.State.ACTIVE.name());
        return cityRepository.save(city);
    }

    @Override
    public City updateCity(City city) {
        Optional<City> cityDB = getCity(city.getId());
        if (cityDB.isPresent()){
            return cityRepository.save(city);
        }
        return null;
    }

    @Override
    public City deleteCity(Long id) {
        Optional<City> cityDB = getCity(id);
        if (cityDB.isPresent()){
            City cityUpdate = cityDB.get();
            cityUpdate.setState(Constant.State.DELETE.name());
            return cityRepository.save(cityUpdate);
        }
        return null;
    }
}
