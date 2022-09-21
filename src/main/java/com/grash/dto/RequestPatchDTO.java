package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
public class RequestPatchDTO {

    private String title;

    private String description;

    private boolean approved;


    private Priority priority;

    private Image image;

    private Long createdBy;

    private Asset asset;

    private Location location;

    private User assignedTo;

    private Date dueDate;

    private String category;

    private Team team;

    private Collection<File> files;
}
