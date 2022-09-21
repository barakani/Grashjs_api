package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
public class WorkOrderPatchDTO {
    private User completedBy;
    private Date completedOn;
    private boolean archived;
    private PurchaseOrder purchaseOrder;
    private Date dueDate;
    private Status status;
    private Priority priority;
    private int estimatedDuration;
    private String description;
    private String title;
    private boolean requiredSignature;

    private Location location;

    private Team team;

    private User primaryUser;

    private Asset asset;

    private Collection<User> assignedTo;

    private Collection<Part> parts;

    private Collection<File> files;
}
