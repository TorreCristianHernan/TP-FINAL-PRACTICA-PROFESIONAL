package hallytech.phoenix.service;

import hallytech.phoenix.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    public List<Address> listAddresses();
    public Optional<Address> getAddress(Long id);
    public Address saveAddress(Address address);
    public Address updateAddress(Address address);
    public Address deleteAddress(Long id);
}
