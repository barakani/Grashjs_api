package com.grash.repository;

import com.grash.model.AdditionalCost;
import com.grash.model.envers.WorkOrderAud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface WorkOrderAudRepository extends JpaRepository<WorkOrderAud, Long> {
    @Query("SELECT w FROM WorkOrderAud w WHERE w.workOrderAudId.id = :id AND w.revtype= :revType")
    List<WorkOrderAud> findByIdAndRevtype(@Param("id")Long id, @Param("revType") Integer revType );
}
