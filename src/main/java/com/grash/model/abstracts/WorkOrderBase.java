package com.grash.model.abstracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.Status;
import lombok.Data;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Part> parts = new ArrayList<>();

    @ManyToOne
    private Location location;

    @ManyToOne
    private Team team;

    @ManyToOne
    private User primaryUser;

    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<User> assignedTo = new ArrayList<>();

    @ManyToOne
    @NotNull
    private Asset asset;

}
