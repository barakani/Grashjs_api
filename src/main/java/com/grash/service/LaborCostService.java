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
    private final LaborCostRepository laborCostRepository;

    public LaborCost create(LaborCost LaborCost) {
        return laborCostRepository.save(LaborCost);
    }

    public LaborCost update(LaborCost LaborCost) {
        return laborCostRepository.save(LaborCost);
    }

    public Collection<LaborCost> getAll() { return laborCostRepository.findAll(); }

    public void delete(Long id){ laborCostRepository.deleteById(id);}

    public Optional<LaborCost> findById(Long id) {return laborCostRepository.findById(id); }
}
