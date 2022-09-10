package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.enums.AssetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
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

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Part> parts;

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

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<User> assignedTo;

    @OneToMany(mappedBy = "asset=", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Team> teams;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Vendor> vendors;

    @OneToOne
    private Deprecation deprecation;

    private Date warrantyExpirationDate;

    private String additionalInfos;

    private AssetStatus status;

    private int uptime;

    private int downtime;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<File> files;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Meter> meters;

}



