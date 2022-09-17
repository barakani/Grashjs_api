package com.grash.repository;

import com.grash.model.PurchaseOrderCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PurchaseOrderCategoryRepository extends JpaRepository<PurchaseOrderCategory, Long> {
    Collection<PurchaseOrderCategory> findByCompanySettings_Id();
}
