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
    private final FieldConfigurationRepository FieldConfigurationRepository;

    private final ModelMapper modelMapper;

    public FieldConfiguration create(FieldConfiguration FieldConfiguration) {
        return FieldConfigurationRepository.save(FieldConfiguration);
    }

    public FieldConfiguration update(FieldConfiguration FieldConfiguration) {
        return FieldConfigurationRepository.save(FieldConfiguration);
    }

    public Collection<FieldConfiguration> getAll() { return FieldConfigurationRepository.findAll(); }

    public void delete(Long id){ FieldConfigurationRepository.deleteById(id);}

    public Optional<FieldConfiguration> findById(Long id) {return FieldConfigurationRepository.findById(id); }
}
