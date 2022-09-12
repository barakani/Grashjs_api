package com.grash.service;

import com.grash.model.PreventiveMaintenance;
import com.grash.repository.PreventiveMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreventiveMaintenanceService {
    private final PreventiveMaintenanceRepository preventiveMaintenanceRepository;

    public PreventiveMaintenance create(PreventiveMaintenance PreventiveMaintenance) {
        return preventiveMaintenanceRepository.save(PreventiveMaintenance);
    }

    public PreventiveMaintenance update(PreventiveMaintenance PreventiveMaintenance) {
        return preventiveMaintenanceRepository.save(PreventiveMaintenance);
    }

    public Collection<PreventiveMaintenance> getAll() { return preventiveMaintenanceRepository.findAll(); }

    public void delete(Long id){ preventiveMaintenanceRepository.deleteById(id);}

    public Optional<PreventiveMaintenance> findById(Long id) {return preventiveMaintenanceRepository.findById(id); }
}
