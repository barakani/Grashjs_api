package com.grash.model;

import com.grash.model.abstracts.CompanyAudit;
import com.grash.model.enums.Priority;
import com.grash.model.enums.WorkflowActionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowAction extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private WorkflowActionEnum workflowActionEnum;
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
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Checklist checklist;
}
