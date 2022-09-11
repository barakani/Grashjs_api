package com.grash.service;

import com.grash.model.AdditionalCost;
import com.grash.repository.AdditionalCostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalCostService {

    private final AdditionalCostRepository additionalCostRepository;

    private final ModelMapper modelMapper;

    public AdditionalCost create(AdditionalCost additionalCost) {
        return additionalCostRepository.save(additionalCost);
    }

    public AdditionalCost update(AdditionalCost additionalCost) {
        return additionalCostRepository.save(additionalCost);
    }

    public Collection<AdditionalCost> getAll() { return additionalCostRepository.findAll(); }

    public void delete(Long id){ additionalCostRepository.deleteById(id);}

    public Optional<AdditionalCost> findById(Long id) {return additionalCostRepository.findById(id); }


}
