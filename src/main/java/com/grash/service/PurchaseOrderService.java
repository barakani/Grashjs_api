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
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrder create(PurchaseOrder PurchaseOrder) {
        return purchaseOrderRepository.save(PurchaseOrder);
    }

    public PurchaseOrder update(PurchaseOrder PurchaseOrder) {
        return purchaseOrderRepository.save(PurchaseOrder);
    }

    public Collection<PurchaseOrder> getAll() { return purchaseOrderRepository.findAll(); }

    public void delete(Long id){ purchaseOrderRepository.deleteById(id);}

    public Optional<PurchaseOrder> findById(Long id) {return purchaseOrderRepository.findById(id); }
}
