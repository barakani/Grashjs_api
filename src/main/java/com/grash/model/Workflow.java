package com.grash.model;

import com.grash.model.abstracts.CompanyAudit;
import com.grash.model.enums.WFMainCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workflow extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private WFMainCondition mainCondition;
    @OneToMany
    private List<WorkflowCondition> secondaryConditions;
    @OneToOne
    private WorkflowAction action;
}
