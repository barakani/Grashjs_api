package com.grash.service;

import com.grash.model.WorkOrder;
import com.grash.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;

    public WorkOrder create(WorkOrder WorkOrder) {
        return workOrderRepository.save(WorkOrder);
    }

    public WorkOrder update(WorkOrder WorkOrder) {
        return workOrderRepository.save(WorkOrder);
    }

    public Collection<WorkOrder> getAll() { return workOrderRepository.findAll(); }

    public void delete(Long id){ workOrderRepository.deleteById(id);}

    public Optional<WorkOrder> findById(Long id) {return workOrderRepository.findById(id); }
}
