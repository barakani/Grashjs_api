package com.grash.repository;

import com.grash.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Collection<Customer> findByCompany_Id(Long id);
}
