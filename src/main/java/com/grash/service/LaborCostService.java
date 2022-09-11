package com.grash.service;

import com.grash.model.LaborCost;
import com.grash.repository.LaborCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaborCostService {
    private final LaborCostRepository LaborCostRepository;

    public LaborCost create(LaborCost LaborCost) {
        return LaborCostRepository.save(LaborCost);
    }

    public LaborCost update(LaborCost LaborCost) {
        return LaborCostRepository.save(LaborCost);
    }

    public Collection<LaborCost> getAll() { return LaborCostRepository.findAll(); }

    public void delete(Long id){ LaborCostRepository.deleteById(id);}

    public Optional<LaborCost> findById(Long id) {return LaborCostRepository.findById(id); }
}
