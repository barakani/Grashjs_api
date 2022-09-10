package com.grash.model;

import com.grash.model.abstracts.Cost;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class LaborCost extends Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull
    private WorkOrder workOrder;
}
