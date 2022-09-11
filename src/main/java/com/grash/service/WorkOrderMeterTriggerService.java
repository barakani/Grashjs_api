package com.grash.service;

import com.grash.model.WorkOrderMeterTrigger;
import com.grash.repository.WorkOrderMeterTriggerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderMeterTriggerService {
    private final WorkOrderMeterTriggerRepository workOrderMeterTriggerRepository;

    public WorkOrderMeterTrigger create(WorkOrderMeterTrigger WorkOrderMeterTrigger) {
        return workOrderMeterTriggerRepository.save(WorkOrderMeterTrigger);
    }

    public WorkOrderMeterTrigger update(WorkOrderMeterTrigger WorkOrderMeterTrigger) {
        return workOrderMeterTriggerRepository.save(WorkOrderMeterTrigger);
    }

    public Collection<WorkOrderMeterTrigger> getAll() { return workOrderMeterTriggerRepository.findAll(); }

    public void delete(Long id){ workOrderMeterTriggerRepository.deleteById(id);}

    public Optional<WorkOrderMeterTrigger> findById(Long id) {return workOrderMeterTriggerRepository.findById(id); }
}
