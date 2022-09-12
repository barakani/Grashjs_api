package com.grash.service;

import com.grash.model.MeterCategory;
import com.grash.repository.MeterCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeterCategoryService {
    private final MeterCategoryRepository meterCategoryRepository;

    private final ModelMapper modelMapper;

    public MeterCategory create(MeterCategory MeterCategory) {
        return meterCategoryRepository.save(MeterCategory);
    }

    public MeterCategory update(MeterCategory MeterCategory) {
        return meterCategoryRepository.save(MeterCategory);
    }

    public Collection<MeterCategory> getAll() { return meterCategoryRepository.findAll(); }

    public void delete(Long id){ meterCategoryRepository.deleteById(id);}

    public Optional<MeterCategory> findById(Long id) {return meterCategoryRepository.findById(id); }
}
