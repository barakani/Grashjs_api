package com.grash.model;

import com.grash.model.abstracts.Cost;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class AdditionalCost extends Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    @OneToOne
    private User assignedTo;

    private boolean includeToTotalCost;
}
