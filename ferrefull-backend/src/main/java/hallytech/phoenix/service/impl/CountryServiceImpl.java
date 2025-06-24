package hallytech.phoenix.service.impl;

import hallytech.phoenix.entity.Country;
import hallytech.phoenix.repository.CountryRepository;
import hallytech.phoenix.service.CountryService;
import hallytech.phoenix.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public List<Country> listCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> getCountry(Long id) {
        return countryRepository.findById(id);
    }

    @Override
    public Country saveCountry(Country country) {
        country.setState(Constant.State.ACTIVE.name());
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(Country component) {
        Optional<Country> countryDB = getCountry(component.getId());
        if (countryDB.isPresent()){
            Country countryUpdate = countryDB.get();
            countryUpdate.setName(component.getName());
            countryUpdate.setState(component.getState());
            return countryRepository.save(countryUpdate);
        }
        return null;
    }

    @Override
    public Country deleteCountry(Long id) {
        Optional<Country> countryDB = getCountry(id);
        if (countryDB.isPresent()){
            Country countryUpdate = countryDB.get();
            countryUpdate.setState(Constant.State.DELETE.name());
            return countryRepository.save(countryUpdate);
        }
        return null;
    }
}
