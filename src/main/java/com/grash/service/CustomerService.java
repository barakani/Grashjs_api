package com.grash.service;

import com.grash.dto.CustomerPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Customer;
import com.grash.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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

    public Customer update(Long id, CustomerPatchDTO customer) {
        if (customerRepository.existsById(id)) {
            Customer savedCustomer = customerRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(customer, savedCustomer);
            return customerRepository.save(savedCustomer);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Customer> getAll() {
        return customerRepository.findAll();
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Collection<Customer> findByCompany(Long id) {
        return customerRepository.findByCompany_Id(id);
    }
}
