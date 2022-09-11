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
    private final CostCategoryRepository CostCategoryRepository;

    private final ModelMapper modelMapper;

    public CostCategory create(CostCategory CostCategory) {
        return CostCategoryRepository.save(CostCategory);
    }

    public CostCategory update(CostCategory CostCategory) {
        return CostCategoryRepository.save(CostCategory);
    }

    public Collection<CostCategory> getAll() { return CostCategoryRepository.findAll(); }

    public void delete(Long id){ CostCategoryRepository.deleteById(id);}

    public Optional<CostCategory> findById(Long id) {return CostCategoryRepository.findById(id); }
}
