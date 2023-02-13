package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.*;
import com.grash.model.enums.workflow.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class WorkflowConditionPatchDTO {
    private WorkOrderCondition workOrderCondition;
    private RequestCondition requestCondition;
    private PurchaseOrderCondition purchaseOrderCondition;
    private PartCondition partCondition;
    private TaskCondition taskCondition;
    private Priority priority;
    private Asset asset;
    private Location location;
    private OwnUser user;
    private Team team;
    private WorkOrderCategory category;
    private Checklist checklist;
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Date dueDateStart;
    private Date dueDateEnd;
    private Vendor vendor;
    private Part part;
    private PurchaseOrderCategory purchaseOrderCategory;
    private Status workOrderStatus;
    private ApprovalStatus purchaseOrderStatus;
    private Date start;
    private Date end;
    private String label;
    private String value;
    private Integer numberValue;
}
