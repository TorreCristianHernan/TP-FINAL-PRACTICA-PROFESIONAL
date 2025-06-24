package hallytech.phoenix.service;

import hallytech.phoenix.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    public List<City> listCities();
    public Optional<City> getCity(Long id);
    public City saveCity(City city);
    public City updateCity(City city);
    public City deleteCity(Long id);
}
