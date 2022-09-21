package com.grash.dto;

import com.grash.model.TaskBase;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class ChecklistPatchDTO {
    private String name;

    private String description;

    private Collection<TaskBase> taskBases;
}
