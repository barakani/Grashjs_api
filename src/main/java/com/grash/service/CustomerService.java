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
    private final CustomerRepository CustomerRepository;

    private final ModelMapper modelMapper;

    public Customer create(Customer Customer) {
        return CustomerRepository.save(Customer);
    }

    public Customer update(Customer Customer) {
        return CustomerRepository.save(Customer);
    }

    public Collection<Customer> getAll() { return CustomerRepository.findAll(); }

    public void delete(Long id){ CustomerRepository.deleteById(id);}

    public Optional<Customer> findById(Long id) {return CustomerRepository.findById(id); }
}
