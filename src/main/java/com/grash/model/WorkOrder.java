package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.abstracts.WorkOrderBase;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private Collection<WorkOrderHistory> workOrderHistoryList;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private Collection<Task> taskList;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private Collection<Labor> labors;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private Collection<AdditionalCost> additionalCosts;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private Collection<AdditionalTime> additionalTimes;
    @OneToOne
    private Request parentRequest;

    @OneToOne
    private PurchaseOrder purchaseOrder;

    @ManyToMany
    @JoinTable(name = "T_WorkOrder_File_Associations",
            joinColumns = @JoinColumn(name = "idWorkOrder"),
            inverseJoinColumns = @JoinColumn(name = "idFile"))
    private Collection<File> fileList;

    @OneToOne
    private PreventiveMaintenance parentPreventiveMaintenance;

}
