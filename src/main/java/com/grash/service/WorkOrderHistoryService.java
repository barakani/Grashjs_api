package com.grash.service;

import com.grash.model.WorkOrderHistory;
import com.grash.repository.WorkOrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkOrderHistoryService {
    private final WorkOrderHistoryRepository WorkOrderHistoryRepository;

    public WorkOrderHistory create(WorkOrderHistory WorkOrderHistory) {
        return WorkOrderHistoryRepository.save(WorkOrderHistory);
    }

    public WorkOrderHistory update(WorkOrderHistory WorkOrderHistory) {
        return WorkOrderHistoryRepository.save(WorkOrderHistory);
    }

    public Collection<WorkOrderHistory> getAll() { return WorkOrderHistoryRepository.findAll(); }

    public void delete(Long id){ WorkOrderHistoryRepository.deleteById(id);}

    public Optional<WorkOrderHistory> findById(Long id) {return WorkOrderHistoryRepository.findById(id); }
}
