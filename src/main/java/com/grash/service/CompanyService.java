package com.grash.service;

import com.grash.model.Company;
import com.grash.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository CompanyRepository;

    private final ModelMapper modelMapper;

    public Company create(Company Company) {
        return CompanyRepository.save(Company);
    }

    public Company update(Company Company) {
        return CompanyRepository.save(Company);
    }

    public Collection<Company> getAll() { return CompanyRepository.findAll(); }

    public void delete(Long id){ CompanyRepository.deleteById(id);}

    public Optional<Company> findById(Long id) {return CompanyRepository.findById(id); }
}
