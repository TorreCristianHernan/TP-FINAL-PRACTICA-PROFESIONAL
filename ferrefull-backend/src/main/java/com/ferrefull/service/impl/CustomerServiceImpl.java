package com.ferrefull.service.impl;

import com.ferrefull.util.Constant;
import com.ferrefull.entity.Customer;
import com.ferrefull.repository.CustomerRepository;
import com.ferrefull.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        customer.setState(Constant.State.ACTIVE.name());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        if (getCustomer(customer.getId()).isPresent()){
            log.info("CUSTOMER EDIT :: {}", customer);
            return customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public Customer deleteCustomer(Long id) {
        Optional<Customer> equipmentDB = getCustomer(id);
        if (equipmentDB.isPresent()){
            Customer customerDelete = equipmentDB.get();
            customerDelete.setState(Constant.State.DELETE.name());
            return customerRepository.save(customerDelete);
        }
        return null;
    }
}
