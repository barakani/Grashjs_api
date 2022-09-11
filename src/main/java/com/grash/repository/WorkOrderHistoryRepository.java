package com.grash.repository;

import com.grash.model.WorkOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderHistoryRepository extends JpaRepository<WorkOrderHistory, Long> {
}
