package com.grash.service;

import com.grash.dto.CustomFieldPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.CustomField;
import com.grash.repository.CustomFieldRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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

    public CustomField update(Long id, CustomFieldPatchDTO customField) {
        if (customFieldRepository.existsById(id)) {
            CustomField savedCustomField = customFieldRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(customField, savedCustomField);
            return customFieldRepository.save(savedCustomField);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<CustomField> getAll() {
        return customFieldRepository.findAll();
    }

    public void delete(Long id) {
        customFieldRepository.deleteById(id);
    }

    public Optional<CustomField> findById(Long id) {
        return customFieldRepository.findById(id);
    }
}
