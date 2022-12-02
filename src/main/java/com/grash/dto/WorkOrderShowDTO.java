package com.grash.dto;

import com.grash.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class WorkOrderShowDTO extends WorkOrderBaseShowDTO {

    private OwnUser completedBy;

    private Date completedOn;

    private boolean archived;

    private List<Task> taskList;

    private Request parentRequest;

    private PreventiveMaintenance parentPreventiveMaintenance;

    private File signature;

}
