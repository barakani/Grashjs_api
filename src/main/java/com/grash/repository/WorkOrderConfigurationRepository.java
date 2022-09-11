package com.grash.repository;

import com.grash.model.WorkOrderConfiguration;
import com.grash.model.WorkOrderRequestConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderConfigurationRepository extends JpaRepository<WorkOrderConfiguration, Long> {
}
