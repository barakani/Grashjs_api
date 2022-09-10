package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @OneToOne
    private MeterCategory meterCategory;

    @OneToOne
    private Image image;

//    assignedTo: list<User>;

    @OneToOne
    private Location location;

    @OneToOne
    private Asset asset;

//    private List<WorkOrderMeterTrigger> workOrderMeterTriggerList;

}
