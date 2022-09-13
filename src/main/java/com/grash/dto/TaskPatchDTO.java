package com.grash.dto;

import com.grash.model.Image;
import com.grash.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class TaskPatchDTO {
    
    private Status status;

    private String note;

    private Collection<Image> images;

}
