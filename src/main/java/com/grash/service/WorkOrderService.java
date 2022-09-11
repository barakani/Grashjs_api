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
    private final WorkOrderRepository WorkOrderRepository;

    public WorkOrder create(WorkOrder WorkOrder) {
        return WorkOrderRepository.save(WorkOrder);
    }

    public WorkOrder update(WorkOrder WorkOrder) {
        return WorkOrderRepository.save(WorkOrder);
    }

    public Collection<WorkOrder> getAll() { return WorkOrderRepository.findAll(); }

    public void delete(Long id){ WorkOrderRepository.deleteById(id);}

    public Optional<WorkOrder> findById(Long id) {return WorkOrderRepository.findById(id); }
}
