package com.grash.service;

import com.grash.model.SingleWorkOrderFieldConfiguration;
import com.grash.repository.SingleWorkOrderFieldConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SingleWorkOrderFieldConfigurationService {
    private final SingleWorkOrderFieldConfigurationRepository SingleWorkOrderFieldConfigurationRepository;

    public SingleWorkOrderFieldConfiguration create(SingleWorkOrderFieldConfiguration SingleWorkOrderFieldConfiguration) {
        return SingleWorkOrderFieldConfigurationRepository.save(SingleWorkOrderFieldConfiguration);
    }

    public SingleWorkOrderFieldConfiguration update(SingleWorkOrderFieldConfiguration SingleWorkOrderFieldConfiguration) {
        return SingleWorkOrderFieldConfigurationRepository.save(SingleWorkOrderFieldConfiguration);
    }

    public Collection<SingleWorkOrderFieldConfiguration> getAll() { return SingleWorkOrderFieldConfigurationRepository.findAll(); }

    public void delete(Long id){ SingleWorkOrderFieldConfigurationRepository.deleteById(id);}

    public Optional<SingleWorkOrderFieldConfiguration> findById(Long id) {return SingleWorkOrderFieldConfigurationRepository.findById(id); }
}
