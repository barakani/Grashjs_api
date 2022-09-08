package com.graphjs.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date dueDate;

    private Long wo;

//    private Status status;

//    private Enum<Priority> priority;

    private double estimatedDuration;

    private String description;

    private String title;

    private boolean requiredSignature;

//    private List<Relation> relationList;

    private boolean repeating = false;

//    private List<Lobor> loborList;

//    private Location location;

//    private List<Part> partList;

//    private Team team;

//    assignedTo: list<User>;

//    private Asset asset;

//    private List<User> additionalWorkers;

    private boolean isScheduled;
}
