package com.grash.service;

import com.grash.dto.CategoryPostDTO;
import com.grash.exception.CustomException;
import com.grash.model.CostCategory;
import com.grash.repository.CostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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

    public CostCategory update(Long id, CategoryPostDTO costCategory) {
        if (costCategoryRepository.existsById(id)) {
            CostCategory savedCostCategory = costCategoryRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(costCategory, savedCostCategory);
            return costCategoryRepository.save(savedCostCategory);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);

    }

    public Collection<CostCategory> getAll() {
        return costCategoryRepository.findAll();
    }

    public void delete(Long id) {
        costCategoryRepository.deleteById(id);
    }

    public Optional<CostCategory> findById(Long id) {
        return costCategoryRepository.findById(id);
    }

    public Collection<CostCategory> findByCompanySettings(Long id) {
        return costCategoryRepository.findByCompanySettings(id);
    }
}
