package com.grash.service;

import com.grash.model.SingleWorkOrderFieldConfiguration;
import com.grash.repository.SingleWorkOrderFieldConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SingleWorkOrderFieldConfigurationService {
    private final SingleWorkOrderFieldConfigurationRepository singleWorkOrderFieldConfigurationRepository;

    public SingleWorkOrderFieldConfiguration create(SingleWorkOrderFieldConfiguration SingleWorkOrderFieldConfiguration) {
        return singleWorkOrderFieldConfigurationRepository.save(SingleWorkOrderFieldConfiguration);
    }

    public SingleWorkOrderFieldConfiguration update(SingleWorkOrderFieldConfiguration SingleWorkOrderFieldConfiguration) {
        return singleWorkOrderFieldConfigurationRepository.save(SingleWorkOrderFieldConfiguration);
    }

    public Collection<SingleWorkOrderFieldConfiguration> getAll() { return singleWorkOrderFieldConfigurationRepository.findAll(); }

    public void delete(Long id){ singleWorkOrderFieldConfigurationRepository.deleteById(id);}

    public Optional<SingleWorkOrderFieldConfiguration> findById(Long id) {return singleWorkOrderFieldConfigurationRepository.findById(id); }
}
