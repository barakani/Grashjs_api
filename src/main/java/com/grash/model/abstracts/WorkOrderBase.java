package com.grash.model.abstracts;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.Status;
import lombok.Data;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class WorkOrderBase extends CompanyAudit {
    private Date dueDate;
    private Status status = Status.OPEN;
    private Priority priority = Priority.NONE;
    private int estimatedDuration;
    private String description;
    private String title;
    private boolean requiredSignature;

    @ManyToMany
    private Collection<Part> parts;

    @ManyToOne
    @NotNull
    private Location location;

    @ManyToOne
    private Team team;

    @ManyToOne
    private User primaryUser;

    @ManyToMany
    private Collection<User> assignedTo;

    @ManyToOne
    @NotNull
    private Asset asset;

}
