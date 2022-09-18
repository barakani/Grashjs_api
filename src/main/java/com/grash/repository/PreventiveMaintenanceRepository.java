package com.grash.repository;

import com.grash.model.PreventiveMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PreventiveMaintenanceRepository extends JpaRepository<PreventiveMaintenance, Long> {
    @Query("SELECT p from PreventiveMaintenance p where p.company.id = :x ")
    Collection<PreventiveMaintenance> findByCompany_Id(@Param("x") Long id);
}
