package com.grash.service;

import com.grash.model.PurchaseOrder;
import com.grash.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository PurchaseOrderRepository;

    public PurchaseOrder create(PurchaseOrder PurchaseOrder) {
        return PurchaseOrderRepository.save(PurchaseOrder);
    }

    public PurchaseOrder update(PurchaseOrder PurchaseOrder) {
        return PurchaseOrderRepository.save(PurchaseOrder);
    }

    public Collection<PurchaseOrder> getAll() { return PurchaseOrderRepository.findAll(); }

    public void delete(Long id){ PurchaseOrderRepository.deleteById(id);}

    public Optional<PurchaseOrder> findById(Long id) {return PurchaseOrderRepository.findById(id); }
}
