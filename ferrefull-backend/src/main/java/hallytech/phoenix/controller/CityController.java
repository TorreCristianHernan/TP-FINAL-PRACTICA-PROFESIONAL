package hallytech.phoenix.controller;

import hallytech.phoenix.entity.City;
import hallytech.phoenix.service.CityService;
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
@RequestMapping(value = "/api/city")
@SecurityRequirement(name = "bearerAuth")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<List<City>> listCities(){
        return ResponseEntity.ok(cityService.listCities());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<City> getCity(@PathVariable("id") Long id){
        return cityService.getCity(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<City> addCity(@Valid @RequestBody City city, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.saveCity(city));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<City> updateCity(@PathVariable("id") Long id, @Valid @RequestBody City city, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        city.setId(id);
        City cityDB = cityService.updateCity(city);
        if (Objects.isNull(cityDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cityDB);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<City> updateCity(@PathVariable("id") Long id){
        City cityDB = cityService.deleteCity(id);
        if (Objects.isNull(cityDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cityDB);
    }

}
