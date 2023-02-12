package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.CompanyAudit;
import com.grash.model.enums.Priority;
import com.grash.model.enums.WFSecondaryCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowCondition extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private WFSecondaryCondition wfSecondaryCondition;
    private Priority priority;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Asset asset;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Location location;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OwnUser user;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team team;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WorkOrderCategory category;
    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Workflow workflow;
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Integer dueDateStart;
    private Integer dueDateEnd;
}
