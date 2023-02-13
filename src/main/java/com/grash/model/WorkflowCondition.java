package com.grash.model;

import com.grash.model.abstracts.CompanyAudit;
import com.grash.model.enums.*;
import com.grash.model.enums.workflow.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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
    private WorkOrderCondition workOrderCondition;
    private RequestCondition requestCondition;
    private PurchaseOrderCondition purchaseOrderCondition;
    private PartCondition partCondition;
    private TaskCondition taskCondition;
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
    private Vendor vendor;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Part part;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WorkOrderCategory workOrderCategory;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PurchaseOrderCategory purchaseOrderCategory;
    private Status workOrderStatus;
    private ApprovalStatus purchaseOrderStatus;
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Date dueDateStart;
    private Date dueDateEnd;
    private Date start;
    private Date end;
    private String label;
    private String value;
    private Integer numberValue;

    public boolean isMetForWorkOrder(WorkOrder workOrder) {
        switch (workOrderCondition) {
            case TEAM_IS:
                return workOrder.getTeam().getId().equals(this.team.getId());
            case PRIORITY_IS:
                return workOrder.getPriority().equals(this.getPriority());
            case ASSET_IS:
                return workOrder.getAsset().getId().equals(this.asset.getId());
            case CATEGORY_IS:
                return workOrder.getCategory().getId().equals(this.workOrderCategory.getId());
            case LOCATION_IS:
                return workOrder.getLocation().getId().equals(this.location.getId());
            case USER_IS:
                return workOrder.getPrimaryUser().getId().equals(this.user.getId());
            case CREATED_AT_BETWEEN:
                return workOrder.getCreatedAt().getHours() > this.getCreatedTimeStart() && workOrder.getCreatedAt().getHours() < (this.getCreatedTimeEnd());
            case DUE_DATE_BETWEEN:
                return workOrder.getDueDate().after(this.getDueDateStart()) && workOrder.getDueDate().before(this.getDueDateEnd());
            default:
                return false;
        }
    }
}
