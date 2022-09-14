package com.grash.repository;

import com.grash.model.CostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CostCategoryRepository extends JpaRepository<CostCategory, Long> {

    Collection<CostCategory> findByCompanySettings_Id(Long id);

}
