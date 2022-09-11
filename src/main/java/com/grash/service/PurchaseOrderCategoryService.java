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
    private final PurchaseOrderCategoryRepository PurchaseOrderCategoryRepository;

    public PurchaseOrderCategory create(PurchaseOrderCategory PurchaseOrderCategory) {
        return PurchaseOrderCategoryRepository.save(PurchaseOrderCategory);
    }

    public PurchaseOrderCategory update(PurchaseOrderCategory PurchaseOrderCategory) {
        return PurchaseOrderCategoryRepository.save(PurchaseOrderCategory);
    }

    public Collection<PurchaseOrderCategory> getAll() { return PurchaseOrderCategoryRepository.findAll(); }

    public void delete(Long id){ PurchaseOrderCategoryRepository.deleteById(id);}

    public Optional<PurchaseOrderCategory> findById(Long id) {return PurchaseOrderCategoryRepository.findById(id); }
}
