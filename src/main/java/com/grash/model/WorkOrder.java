package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.abstracts.WorkOrderBase;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class WorkOrder extends WorkOrderBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User completedBy;

    private Date completedOn;

    private boolean archived;

    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY)
    private List<Task> taskList = new ArrayList<>();

    @ManyToOne
    private Request parentRequest;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(name = "T_WorkOrder_File_Associations",
            joinColumns = @JoinColumn(name = "id_work_order"),
            inverseJoinColumns = @JoinColumn(name = "id_file"),
            indexes = {
                    @Index(name = "idx_work_order_file_work_order_id", columnList = "id_work_order"),
                    @Index(name = "idx_work_order_file_file_id", columnList = "id_file")
            })
    private List<File> files = new ArrayList<>();

    @ManyToOne
    private PreventiveMaintenance parentPreventiveMaintenance;

}
