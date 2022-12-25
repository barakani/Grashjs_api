package com.grash.repository;

import com.grash.model.PartConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;

public interface PartConsumptionRepository extends JpaRepository<PartConsumption, Long> {
    Collection<PartConsumption> findByCompany_Id(Long id);

    Collection<PartConsumption> findByWorkOrder_Id(Long id);

    Collection<PartConsumption> findByPart_Id(Long id);

    Collection<PartConsumption> findByCreatedAtBetweenAndCompany_Id(Instant date1, Instant date2, Long companyId);


}
