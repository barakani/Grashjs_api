package com.grash.repository;

import com.grash.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Collection<PurchaseOrder> findByCompany_Id(@Param("x") Long id);
}
