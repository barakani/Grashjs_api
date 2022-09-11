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
    private final MeterCategoryRepository MeterCategoryRepository;

    private final ModelMapper modelMapper;

    public MeterCategory create(MeterCategory MeterCategory) {
        return MeterCategoryRepository.save(MeterCategory);
    }

    public MeterCategory update(MeterCategory MeterCategory) {
        return MeterCategoryRepository.save(MeterCategory);
    }

    public Collection<MeterCategory> getAll() { return MeterCategoryRepository.findAll(); }

    public void delete(Long id){ MeterCategoryRepository.deleteById(id);}

    public Optional<MeterCategory> findById(Long id) {return MeterCategoryRepository.findById(id); }
}
