package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.enums.AssetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
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

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Part> parts;

    @OneToOne
    private Image image;

    @ManyToOne
    @NotNull
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

    @ManyToMany
    @JoinTable( name = "T_Asset_User_Associations",
            joinColumns = @JoinColumn( name = "idAsset" ),
            inverseJoinColumns = @JoinColumn( name = "idUser" ) )
    private Collection<User> assignedTo;

    @ManyToMany
    @JoinTable( name = "T_Asset_Team_Associations",
            joinColumns = @JoinColumn( name = "idAsset" ),
            inverseJoinColumns = @JoinColumn( name = "idTeam" ) )
    private Collection<Team> teams;

    @ManyToMany
    @JoinTable( name = "T_Asset_Vendor_Associations",
            joinColumns = @JoinColumn( name = "idAsset" ),
            inverseJoinColumns = @JoinColumn( name = "idVendor" ) )
    private Collection<Vendor> vendors;

    @OneToOne
    private Deprecation deprecation;

    private Date warrantyExpirationDate;

    private String additionalInfos;

    private AssetStatus status = AssetStatus.OPERATIONAL;

    private int uptime;

    private int downtime;

    @ManyToMany
    @JoinTable( name = "T_Asset_File_Associations",
            joinColumns = @JoinColumn( name = "idAsset" ),
            inverseJoinColumns = @JoinColumn( name = "idFile" ) )
    private Collection<File> files;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Meter> meters;

}



