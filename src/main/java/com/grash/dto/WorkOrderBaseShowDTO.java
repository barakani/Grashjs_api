package com.grash.dto;

import com.grash.model.Asset;
import com.grash.model.Location;
import com.grash.model.OwnUser;
import com.grash.model.Team;
import com.grash.model.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class WorkOrderBaseShowDTO extends AuditShowDTO {
    private Long id;

    private Date dueDate;
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

    private List<FileMiniDTO> files;

}
