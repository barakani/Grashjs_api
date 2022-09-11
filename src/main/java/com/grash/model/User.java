package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private double rate;

    private String email;

    private String phone;

    @OneToOne
    private Role role;

    private String jobTitle;

    private String username;

    @JsonIgnore
    private String password;

    private boolean enabled;

    @OneToOne
    private Company company;

    @OneToOne
    private UserSettings userSettings;

    @ManyToMany
    @JoinTable( name = "T_Asset_User_Associations",
            joinColumns = @JoinColumn( name = "idUser" ),
            inverseJoinColumns = @JoinColumn( name = "idAsset" ) )
    private Collection<Asset> asset;

    @ManyToMany
    @JoinTable( name = "T_Location_User_Associations",
            joinColumns = @JoinColumn( name = "idUser" ),
            inverseJoinColumns = @JoinColumn( name = "idLocation" ) )
    private Collection<Location> locations;

    @ManyToMany
    @JoinTable( name = "T_Meter_User_Associations",
            joinColumns = @JoinColumn( name = "idUser" ),
            inverseJoinColumns = @JoinColumn( name = "idMeter" ) )
    private Collection<Meter> meters;

    @ManyToMany
    @JoinTable( name = "T_Part_User_Associations",
            joinColumns = @JoinColumn( name = "idUser" ),
            inverseJoinColumns = @JoinColumn( name = "idPart" ) )
    private Collection<Part> parts;

    @ManyToMany
    @JoinTable( name = "T_Team_User_Associations",
            joinColumns = @JoinColumn( name = "idUser" ),
            inverseJoinColumns = @JoinColumn( name = "idTeam" ) )
    private Collection<Team> teams;





}

