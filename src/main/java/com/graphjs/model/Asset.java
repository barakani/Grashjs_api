package com.graphjs.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createAt;

    private boolean archived;

//    private List<Part> parts;

//    private Image image;

//    private Location location;

//    Parent: Asset;

    private String area;

    private String barCode;

//    category: AssetCategory;

    private String description;

//    primaryUser: Person;

//    assignedTo: list<User>;

//    assignedTeams: list<Team>;

//    assignedVendors: list<Vendor>;

//    private Deprecation deprecation;

    private Date warrantyExpirationDate;

    private String additionalInfos;

//    private AssetStatus status;

//    private AssetReliability reliability;

    private int uptime;

    private int downtime;

//    private List<File> files;

//    private List<Meter> meters;

}



