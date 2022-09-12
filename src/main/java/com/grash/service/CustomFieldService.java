package com.grash.service;

import com.grash.model.CustomField;
import com.grash.repository.CustomFieldRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomFieldService {
    private final CustomFieldRepository customFieldRepository;

    private final ModelMapper modelMapper;

    public CustomField create(CustomField CustomField) {
        return customFieldRepository.save(CustomField);
    }

    public CustomField update(CustomField CustomField) {
        return customFieldRepository.save(CustomField);
    }

    public Collection<CustomField> getAll() { return customFieldRepository.findAll(); }

    public void delete(Long id){ customFieldRepository.deleteById(id);}

    public Optional<CustomField> findById(Long id) {return customFieldRepository.findById(id); }
}
