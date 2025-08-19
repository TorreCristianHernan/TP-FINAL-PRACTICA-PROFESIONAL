package com.ferrefull.service;

import com.ferrefull.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    public List<Country> listCountries();
    public Optional<Country> getCountry(Long id);
    public Country saveCountry(Country country);
    public Country updateCountry(Country country);
    public Country deleteCountry(Long id);
}
