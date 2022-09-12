package com.grash.service;

import com.grash.model.Customer;
import com.grash.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    public Customer create(Customer Customer) {
        return customerRepository.save(Customer);
    }

    public Customer update(Customer Customer) {
        return customerRepository.save(Customer);
    }

    public Collection<Customer> getAll() { return customerRepository.findAll(); }

    public void delete(Long id){ customerRepository.deleteById(id);}

    public Optional<Customer> findById(Long id) {return customerRepository.findById(id); }
}
