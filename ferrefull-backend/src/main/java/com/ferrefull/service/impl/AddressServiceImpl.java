package com.ferrefull.service.impl;

import com.ferrefull.entity.Address;
import com.ferrefull.repository.AddressRepository;
import com.ferrefull.service.AddressService;
import com.ferrefull.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    @Override
    public List<Address> listAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Optional<Address> getAddress(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public Address saveAddress(Address address) {
        address.setState(Constant.State.ACTIVE.name());
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Address address) {
        Optional<Address> categoryDB = getAddress(address.getId());
        if (categoryDB.isPresent()){
            Address addressUpdate = categoryDB.get();
            addressUpdate.setState(address.getState());
            return addressRepository.save(addressUpdate);
        }
        return null;
    }

    @Override
    public Address deleteAddress(Long id) {
        Optional<Address> addressDB = getAddress(id);
        if (addressDB.isPresent()){
            Address addressUpdate = addressDB.get();
            addressUpdate.setState(Constant.State.DELETE.name());
            return addressRepository.save(addressUpdate);
        }
        return null;
    }
}
