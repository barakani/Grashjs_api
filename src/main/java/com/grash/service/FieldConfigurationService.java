package com.grash.service;

import com.grash.model.FieldConfiguration;
import com.grash.repository.FieldConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FieldConfigurationService {
    private final FieldConfigurationRepository fieldConfigurationRepository;

    private final ModelMapper modelMapper;

    public FieldConfiguration create(FieldConfiguration FieldConfiguration) {
        return fieldConfigurationRepository.save(FieldConfiguration);
    }

    public FieldConfiguration update(FieldConfiguration FieldConfiguration) {
        return fieldConfigurationRepository.save(FieldConfiguration);
    }

    public Collection<FieldConfiguration> getAll() { return fieldConfigurationRepository.findAll(); }

    public void delete(Long id){ fieldConfigurationRepository.deleteById(id);}

    public Optional<FieldConfiguration> findById(Long id) {return fieldConfigurationRepository.findById(id); }
}
