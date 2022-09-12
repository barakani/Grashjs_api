package com.grash.service;

import com.grash.model.PurchaseOrderCategory;
import com.grash.repository.PurchaseOrderCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderCategoryService {
    private final PurchaseOrderCategoryRepository purchaseOrderCategoryRepository;

    public PurchaseOrderCategory create(PurchaseOrderCategory PurchaseOrderCategory) {
        return purchaseOrderCategoryRepository.save(PurchaseOrderCategory);
    }

    public PurchaseOrderCategory update(PurchaseOrderCategory PurchaseOrderCategory) {
        return purchaseOrderCategoryRepository.save(PurchaseOrderCategory);
    }

    public Collection<PurchaseOrderCategory> getAll() { return purchaseOrderCategoryRepository.findAll(); }

    public void delete(Long id){ purchaseOrderCategoryRepository.deleteById(id);}

    public Optional<PurchaseOrderCategory> findById(Long id) {return purchaseOrderCategoryRepository.findById(id); }
}
