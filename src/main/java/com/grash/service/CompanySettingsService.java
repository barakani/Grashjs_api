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
    private final CompanySettingsRepository CompanySettingsRepository;

    private final ModelMapper modelMapper;

    public CompanySettings create(CompanySettings CompanySettings) {
        return CompanySettingsRepository.save(CompanySettings);
    }

    public CompanySettings update(CompanySettings CompanySettings) {
        return CompanySettingsRepository.save(CompanySettings);
    }

    public Collection<CompanySettings> getAll() { return CompanySettingsRepository.findAll(); }

    public void delete(Long id){ CompanySettingsRepository.deleteById(id);}

    public Optional<CompanySettings> findById(Long id) {return CompanySettingsRepository.findById(id); }
}
