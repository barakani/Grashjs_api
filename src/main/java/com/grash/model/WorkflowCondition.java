package com.grash.model;

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
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowCondition extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
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
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Date dueDateStart;
    private Date dueDateEnd;

    public boolean isMetForWorkOrder(WorkOrder workOrder) {
        switch (wfSecondaryCondition) {
            case WORK_ORDER_TEAM_IS:
                return workOrder.getTeam().getId().equals(this.team.getId());
            case WORK_ORDER_PRIORITY_IS:
                return workOrder.getPriority().equals(this.getPriority());
            case WORK_ORDER_ASSET_IS:
                return workOrder.getAsset().getId().equals(this.asset.getId());
            case WORK_ORDER_CATEGORY_IS:
                return workOrder.getCategory().getId().equals(this.category.getId());
            case WORK_ORDER_LOCATION_IS:
                return workOrder.getLocation().getId().equals(this.location.getId());
            case WORK_ORDER_USER_IS:
                return workOrder.getPrimaryUser().getId().equals(this.user.getId());
            case WORK_ORDER_CREATED_AT_BETWEEN:
                return workOrder.getCreatedAt().getHours() > this.getCreatedTimeStart() && workOrder.getCreatedAt().getHours() < (this.getCreatedTimeEnd());
            case WORK_ORDER_DUE_DATE_BETWEEN:
                return workOrder.getDueDate().after(this.getDueDateStart()) && workOrder.getDueDate().before(this.getDueDateEnd());
            default:
                return false;
        }
    }
}
