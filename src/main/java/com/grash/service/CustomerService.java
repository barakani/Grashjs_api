package com.grash.service;

import com.grash.dto.CustomerPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.Customer;
import com.grash.model.User;
import com.grash.model.enums.RoleType;
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
    private final LocationService locationService;
    private final ImageService imageService;
    private final AssetCategoryService assetCategoryService;
    private final DeprecationService deprecationService;
    private final UserService userService;
    private final CompanyService companyService;

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

    public boolean hasAccess(User user, Customer customer) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(customer.getCompany().getId());
    }

    public boolean canCreate(User user, Customer customerReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(customerReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        if (first && canPatch(user, modelMapper.map(customerReq, CustomerPatchDTO.class))) {
            return true;
        } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

    public boolean canPatch(User user, CustomerPatchDTO customerReq) {
        return true;
    }
}
