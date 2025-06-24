package hallytech.phoenix.service;

import hallytech.phoenix.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public List<Customer> listCustomers();
    public Optional<Customer> getCustomer(Long id);
    public Customer saveCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
    public Customer deleteCustomer(Long id);
}
