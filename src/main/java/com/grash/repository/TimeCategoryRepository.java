package com.grash.repository;

import com.grash.model.TimeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TimeCategoryRepository extends JpaRepository<TimeCategory, Long> {
    Collection<TimeCategory> findByCompanySettings_Id(Long id);
}
