package hallytech.phoenix.controller;

import hallytech.phoenix.entity.Address;
import hallytech.phoenix.service.AddressService;
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
@RequestMapping(value = "/api/address")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> listAddresses(){
        return ResponseEntity.ok(addressService.listAddresses());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Address> getAddress(@PathVariable("id") Long id){
        return addressService.getAddress(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Address> addAddress(@Valid @RequestBody Address address, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.saveAddress(address));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Address> updateAddress(@PathVariable("id") Long id, @Valid @RequestBody Address address, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        address.setId(id);
        Address addressDB = addressService.updateAddress(address);
        if (Objects.isNull(addressDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(addressDB);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Address> updateAddress(@PathVariable("id") Long id){
        Address addressDB = addressService.deleteAddress(id);
        if (Objects.isNull(addressDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(addressDB);
    }

}
