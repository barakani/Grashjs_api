package com.grash.repository;

import com.grash.model.CostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostCategoryRepository extends JpaRepository<CostCategory, Long> {
}
