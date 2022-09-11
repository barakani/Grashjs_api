package com.grash.model.abstracts;

import com.grash.model.enums.Priority;
import com.grash.model.enums.Status;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
public class WorkOrderBase extends Audit {
    private Date dueDate;
    private Status status = Status.OPEN;
    private Priority priority = Priority.NONE;
    private int estimatedDuration;
    private String description;
    private String title;
    private boolean requiredSignature;
    // private Collection<Relation> relations;
    // private Collection<Labor> labors;
    // private Collection<Part> parts;
    // private Location location;
    //private Team team;
    // private Collection<User> assignedTo;
    // private Collection<File> files;
    //private Asset asset;
    // private Collection<User> additionalWorkers;

}
