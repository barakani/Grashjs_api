package com.grash.model;
import com.grash.model.abstracts.WorkOrderBase;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToOne
    private Request parentRequest;

    @OneToOne
    private PurchaseOrder purchaseOrder;

//    private List<File> fileList;

    @OneToOne
   private PreventiveMaintenance parentPreventiveMaintenance;

}
