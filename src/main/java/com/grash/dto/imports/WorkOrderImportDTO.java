package com.grash.dto.imports;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class WorkOrderImportDTO {

    private Long id;
    private String dueDate;
    private String priority;
    private int estimatedDuration;
    private String description;
    @NotNull
    private String title;
    private boolean requiredSignature;
    private String category;

    private String locationName;

    private String teamName;

    private String primaryUserEmail;

    private List<String> assignedToEmails;

    private String assetName;

    private String completedByEmail;
    private String completedOn;
    private boolean archived;
    private String status;
    private String feedback;
}
