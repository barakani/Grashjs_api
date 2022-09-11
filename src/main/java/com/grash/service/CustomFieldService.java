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
    private final CustomFieldRepository CustomFieldRepository;

    private final ModelMapper modelMapper;

    public CustomField create(CustomField CustomField) {
        return CustomFieldRepository.save(CustomField);
    }

    public CustomField update(CustomField CustomField) {
        return CustomFieldRepository.save(CustomField);
    }

    public Collection<CustomField> getAll() { return CustomFieldRepository.findAll(); }

    public void delete(Long id){ CustomFieldRepository.deleteById(id);}

    public Optional<CustomField> findById(Long id) {return CustomFieldRepository.findById(id); }
}
