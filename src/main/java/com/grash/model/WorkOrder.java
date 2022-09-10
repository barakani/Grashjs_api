package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.Cost;
import com.grash.model.abstracts.WorkOrderBase;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
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

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<WorkOrderHistory> workOrderHistoryList;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<LaborCost> laborCosts;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Task> taskList;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Labor> labors;

    @OneToOne
    private Request parentRequest;

    @OneToOne
    private PurchaseOrder purchaseOrder;

    @ManyToMany
    @JoinTable( name = "T_WorkOrder_File_Associations",
            joinColumns = @JoinColumn( name = "idWorkOrder" ),
            inverseJoinColumns = @JoinColumn( name = "idFile" ) )
    private Collection<File> fileList;

    @OneToOne
    private PreventiveMaintenance parentPreventiveMaintenance;

}
