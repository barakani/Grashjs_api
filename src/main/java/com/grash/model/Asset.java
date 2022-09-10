package com.grash.model;

import com.grash.model.enums.AssetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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

    @OneToOne
    private Image image;

    @OneToOne
    private Location location;

    @OneToOne
    private Asset parentAsset;

    private String area;

    private String barCode;

    @OneToOne
    private AssetCategory category;

    private String description;

    @OneToOne
    private User primaryUser;

//    assignedTo: list<User>;

//    assignedTeams: list<Team>;

//    assignedVendors: list<Vendor>;

    @OneToOne
    private Deprecation deprecation;

    private Date warrantyExpirationDate;

    private String additionalInfos;

    private AssetStatus status;

    private int uptime;

    private int downtime;

//    private List<File> files;

//    private List<Meter> meters;

}



