package com.grash.service;

import com.grash.model.CompanySettings;
import com.grash.repository.CompanySettingsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanySettingsService {
    private final CompanySettingsRepository companySettingsRepository;

    private final ModelMapper modelMapper;

    public CompanySettings create(CompanySettings CompanySettings) {
        return companySettingsRepository.save(CompanySettings);
    }

    public CompanySettings update(CompanySettings CompanySettings) {
        return companySettingsRepository.save(CompanySettings);
    }

    public Collection<CompanySettings> getAll() { return companySettingsRepository.findAll(); }

    public void delete(Long id){ companySettingsRepository.deleteById(id);}

    public Optional<CompanySettings> findById(Long id) {return companySettingsRepository.findById(id); }
}
