package com.grash.service;

import com.grash.dto.FloorPlanPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.FloorPlan;
import com.grash.repository.FloorPlanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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

    public FloorPlan update(Long id, FloorPlanPatchDTO floorPlan) {
        if (floorPlanRepository.existsById(id)) {
            FloorPlan savedFloorPlan = floorPlanRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(floorPlan, savedFloorPlan);
            return floorPlanRepository.save(savedFloorPlan);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<FloorPlan> getAll() {
        return floorPlanRepository.findAll();
    }

    public void delete(Long id) {
        floorPlanRepository.deleteById(id);
    }

    public Optional<FloorPlan> findById(Long id) {
        return floorPlanRepository.findById(id);
    }
}
