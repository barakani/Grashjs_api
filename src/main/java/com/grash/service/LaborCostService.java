package com.grash.service;

import com.grash.dto.LaborCostPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.LaborCost;
import com.grash.repository.LaborCostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaborCostService {
    private final LaborCostRepository laborCostRepository;
    private final ModelMapper modelMapper;

    public LaborCost create(LaborCost LaborCost) {
        return laborCostRepository.save(LaborCost);
    }

    public LaborCost update(Long id, LaborCostPatchDTO laborCostDTO) {
        if (laborCostRepository.existsById(id)) {
            LaborCost savedLaborCost = laborCostRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(laborCostDTO, savedLaborCost);
            return laborCostRepository.save(savedLaborCost);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<LaborCost> getAll() {
        return laborCostRepository.findAll();
    }

    public void delete(Long id) {
        laborCostRepository.deleteById(id);
    }

    public Optional<LaborCost> findById(Long id) {
        return laborCostRepository.findById(id);
    }

    public Collection<LaborCost> findByCompany(Long id) {
        return laborCostRepository.findByCompany_Id(id);
    }
}
