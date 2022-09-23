package com.grash.repository;

import com.grash.model.PreventiveMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PreventiveMaintenanceRepository extends JpaRepository<PreventiveMaintenance, Long> {
    Collection<PreventiveMaintenance> findByCompany_Id(@Param("x") Long id);
}
