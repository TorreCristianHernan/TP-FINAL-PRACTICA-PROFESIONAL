package hallytech.phoenix.controller;

import hallytech.phoenix.entity.Country;
import hallytech.phoenix.service.CountryService;
import hallytech.phoenix.util.Util;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/api/country")
@SecurityRequirement(name = "bearerAuth")
public class CountryController {

    @Autowired
    private CountryService countryService;

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<Country>> listCountries(){
        return ResponseEntity.ok(countryService.listCountries());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Country> getCountry(@PathVariable("id") Long id){
        return countryService.getCountry(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Country> addCountry(@Valid @RequestBody Country country, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.saveCountry(country));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Country> updateCountry(@PathVariable("id") Long id, @Valid @RequestBody Country country, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        country.setId(id);
        Country countryDB = countryService.updateCountry(country);
        if (Objects.isNull(countryDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countryDB);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Country> updateCountry(@PathVariable("id") Long id){
        Country countryDB = countryService.deleteCountry(id);
        if (Objects.isNull(countryDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countryDB);
    }

}
