package com.grash.service;

import com.grash.model.FloorPlan;
import com.grash.repository.FloorPlanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FloorPlanService {
    private final FloorPlanRepository floorPlanRepository;

    private final ModelMapper modelMapper;

    public FloorPlan create(FloorPlan FloorPlan) {
        return floorPlanRepository.save(FloorPlan);
    }

    public FloorPlan update(FloorPlan FloorPlan) {
        return floorPlanRepository.save(FloorPlan);
    }

    public Collection<FloorPlan> getAll() { return floorPlanRepository.findAll(); }

    public void delete(Long id){ floorPlanRepository.deleteById(id);}

    public Optional<FloorPlan> findById(Long id) {return floorPlanRepository.findById(id); }
}
