package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String gps;

//    private List<Asset> assetList;

//    private List<User> workers;

//    private List<Team> teamList;

    @OneToOne
    private Location parentLocation;

    @OneToOne
    private Vendor vendor;

    @OneToOne
    private Customer customer;

//    private List<Part> partList;

//    private List<FloorPlan> floorPlanList;
}

