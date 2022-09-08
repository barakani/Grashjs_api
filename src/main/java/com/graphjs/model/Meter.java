package com.graphjs.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Meter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private List<Reading> readingList;

    private String name;

    private String unit;

    private int updateFrequency;

//    private MeterCategory meterCategory;

//    private Image image;

//    assignedTo: list<User>;

//    private Location location;

//    private Asset asset;

//    private List<WorkOrderMeterTrigger> workOrderMeterTriggerList;

}
