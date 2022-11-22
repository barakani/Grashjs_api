package com.grash.model;

import com.grash.model.abstracts.CompanyAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class PartQuantity extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int quantity;

    @ManyToOne
    @NotNull
    private Part part;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    private WorkOrder workOrder;
}
