package com.grash.repository;

import com.grash.model.LaborCost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface LaborCostRepository extends JpaRepository<LaborCost, Long> {
    Collection<LaborCost> findByCompany_Id(Long id);
}
