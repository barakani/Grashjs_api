package com.grash.model;
import com.grash.model.abstracts.WorkOrderBase;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class WorkOrder extends WorkOrderBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long completedBy;

    private Date completedOn;

    private boolean archived;

//    private List<WorkOrderHistory> workOrderHistoryList;

//    private List<Cost> costList;

//    private List<Task> taskList;

//    private List<Time> timeList;

    //    private Request parentRequest;

    //    private PurchaseOrder purchaseOrder;

//    private List<File> fileList;

//    private PreventiveMaintenance parentPreventiveMaintenance;

}
