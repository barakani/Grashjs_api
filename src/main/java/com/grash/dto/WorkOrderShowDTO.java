package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class WorkOrderShowDTO extends AuditShowDTO {
    private Long id;

    private OwnUser completedBy;

    private Date completedOn;

    private boolean archived;

    private List<Task> taskList;

    private Request parentRequest;

    //TODO
    //private List<File> files = new ArrayList<>();

    private PreventiveMaintenance parentPreventiveMaintenance;

    private Date dueDate;
    private Status status = Status.OPEN;
    private Priority priority = Priority.NONE;
    private int estimatedDuration;
    private String description;
    private String title;
    private boolean requiredSignature;

    private Location location;

    private Team team;

    private OwnUser primaryUser;


    private List<UserMiniDTO> assignedTo;

    private List<CustomerMiniDTO> customers;

    private Asset asset;
    private List<PartMiniDTO> parts;

    private List<FileMiniDTO> files;

}
