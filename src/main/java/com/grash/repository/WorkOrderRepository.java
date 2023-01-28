package com.grash.repository;

import com.grash.model.WorkOrder;
import com.grash.model.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Collection<WorkOrder> findByCompany_Id(Long id);

    Collection<WorkOrder> findByAsset_Id(Long id);

    Collection<WorkOrder> findByLocation_Id(Long id);

    Collection<WorkOrder> findByParentPreventiveMaintenance_Id(Long id);

    Collection<WorkOrder> findByPrimaryUser_Id(Long id);

    Collection<WorkOrder> findByCompletedBy_Id(Long id);

    Collection<WorkOrder> findByPriorityAndCompany_Id(Priority priority, Long companyId);

    Collection<WorkOrder> findByCategory_Id(Long id);

    Collection<WorkOrder> findByCompletedOnBetweenAndCompany_Id(Date date1, Date date2, Long id);

    Collection<WorkOrder> findByCreatedBy(Long id);
}
