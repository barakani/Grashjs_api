package com.graphjs.model.abstracts;

import com.graphjs.model.enums.Priority;
import com.graphjs.model.enums.Status;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
public class WorkOrderBase extends Audit {
    private Date dueDate;
    private Status status;
    private Priority priority;
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