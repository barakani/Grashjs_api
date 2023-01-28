package com.grash.service;

import com.grash.dto.CustomerPatchDTO;
import com.grash.exception.CustomException;
import com.grash.mapper.CustomerMapper;
import com.grash.model.Customer;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CompanyService companyService;
    private final CustomerMapper customerMapper;

    public Customer create(Customer Customer) {
        return customerRepository.save(Customer);
    }

    public Customer update(Long id, CustomerPatchDTO customer) {
        if (customerRepository.existsById(id)) {
            Customer savedCustomer = customerRepository.findById(id).get();
            return customerRepository.save(customerMapper.updateCustomer(savedCustomer, customer));
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

    public boolean hasAccess(OwnUser user, Customer customer) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(customer.getCompany().getId());
    }

    public boolean canCreate(OwnUser user, Customer customerReq) {
        Long companyId = user.getCompany().getId();
        //@NotNull fields
        boolean first = companyService.isCompanyValid(customerReq.getCompany(), companyId);
        return first && canPatch(user, customerMapper.toPatchDto(customerReq));
    }

    public boolean canPatch(OwnUser user, CustomerPatchDTO customerReq) {
        return true;
    }

    public boolean isCustomerInCompany(Customer customer, long companyId, boolean optional) {
        if (optional) {
            Optional<Customer> optionalCustomer = customer == null ? Optional.empty() : findById(customer.getId());
            return customer == null || (optionalCustomer.isPresent() && optionalCustomer.get().getCompany().getId().equals(companyId));
        } else {
            Optional<Customer> optionalCustomer = findById(customer.getId());
            return optionalCustomer.isPresent() && optionalCustomer.get().getCompany().getId().equals(companyId);
        }
    }
}
