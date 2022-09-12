package com.grash.service;

import com.grash.model.CostCategory;
import com.grash.repository.CostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CostCategoryService {
    private final CostCategoryRepository costCategoryRepository;

    private final ModelMapper modelMapper;

    public CostCategory create(CostCategory CostCategory) {
        return costCategoryRepository.save(CostCategory);
    }

    public CostCategory update(CostCategory CostCategory) {
        return costCategoryRepository.save(CostCategory);
    }

    public Collection<CostCategory> getAll() { return costCategoryRepository.findAll(); }

    public void delete(Long id){ costCategoryRepository.deleteById(id);}

    public Optional<CostCategory> findById(Long id) {return costCategoryRepository.findById(id); }
}
