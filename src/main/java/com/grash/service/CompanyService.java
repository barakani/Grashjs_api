package com.grash.service;

import com.grash.model.Company;
import com.grash.model.OwnUser;
import com.grash.model.enums.RoleType;
import com.grash.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Company create(Company Company) {
        return companyRepository.save(Company);
    }

    public Company update(Company Company) {
        return companyRepository.save(Company);
    }

    public Collection<Company> getAll() {
        return companyRepository.findAll();
    }

    public void delete(Long id) {
        companyRepository.deleteById(id);
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public boolean hasAccess(OwnUser user, Company company) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(company.getId());
    }
}
