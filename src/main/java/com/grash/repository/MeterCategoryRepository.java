package com.grash.repository;

import com.grash.model.MeterCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface MeterCategoryRepository extends JpaRepository<MeterCategory, Long> {
    @Query("SELECT m from MeterCategory m where m.companySettings.company.id = :x ")
    Collection<MeterCategory> findByCompany_Id(@Param("x") Long id);
}
