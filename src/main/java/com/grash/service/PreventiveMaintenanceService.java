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
    private final PreventiveMaintenanceRepository PreventiveMaintenanceRepository;

    public PreventiveMaintenance create(PreventiveMaintenance PreventiveMaintenance) {
        return PreventiveMaintenanceRepository.save(PreventiveMaintenance);
    }

    public PreventiveMaintenance update(PreventiveMaintenance PreventiveMaintenance) {
        return PreventiveMaintenanceRepository.save(PreventiveMaintenance);
    }

    public Collection<PreventiveMaintenance> getAll() { return PreventiveMaintenanceRepository.findAll(); }

    public void delete(Long id){ PreventiveMaintenanceRepository.deleteById(id);}

    public Optional<PreventiveMaintenance> findById(Long id) {return PreventiveMaintenanceRepository.findById(id); }
}
