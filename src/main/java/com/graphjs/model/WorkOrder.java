package com.graphjs.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.coyote.RequestInfo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long completedBy;

    private Date completedOn;

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

    private boolean archived;

    // ON N'A PAS BESOIN DE TOUT CE QUI SUIT

//    private List<WorkOrderHistory> workOrderHistoryList;

//    private List<Lobor> loborList;

//    private Location location;

//    private List<Part> partList;

//    private List<Cost> costList;

//    private List<Task> taskList;

//    private List<Time> timeList;

//    private Request fromRequest;

//    private Team team;

//    private PurchaseOrder purchaseOrder;

//    assignedTo: list<User>;

//    private List<File> fileList;

//    private Asset asset;

//    private List<User> additionalWorkers;

    private boolean isScheduled;
}
