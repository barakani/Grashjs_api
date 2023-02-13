package com.grash.model;

import com.grash.model.abstracts.CompanyAudit;
import com.grash.model.enums.WFMainCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workflow extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private WFMainCondition mainCondition;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<WorkflowCondition> secondaryConditions = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    private WorkflowAction action;
}
