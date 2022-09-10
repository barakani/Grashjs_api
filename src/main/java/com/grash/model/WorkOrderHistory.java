package com.grash.model;

import com.grash.model.abstracts.Audit;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderHistory extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @NotNull
    private WorkOrder workOrder;

}
