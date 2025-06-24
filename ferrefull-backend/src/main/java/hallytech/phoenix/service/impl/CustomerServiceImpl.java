package hallytech.phoenix.service.impl;

import hallytech.phoenix.entity.Customer;
import hallytech.phoenix.repository.CustomerRepository;
import hallytech.phoenix.service.CustomerService;
import hallytech.phoenix.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
