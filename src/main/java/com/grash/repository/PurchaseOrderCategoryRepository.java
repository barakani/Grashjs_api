package com.grash.repository;

import com.grash.model.PurchaseOrderCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderCategoryRepository extends JpaRepository<PurchaseOrderCategory, Long> {
}
