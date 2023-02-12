package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowAction extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne
    private Workflow workflow;
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Integer dueDateStart;
    private Integer dueDateEnd;
}
