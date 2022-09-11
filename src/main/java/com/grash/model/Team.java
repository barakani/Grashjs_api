package com.grash.model;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable( name = "T_Team_User_Associations",
            joinColumns = @JoinColumn( name = "idTeam" ),
            inverseJoinColumns = @JoinColumn( name = "idUser" ) )
    private Collection<User> users;

    private String name;

    private String description;

    @ManyToMany
    @JoinTable( name = "T_Team_Asset_Associations",
            joinColumns = @JoinColumn( name = "idTeam" ),
            inverseJoinColumns = @JoinColumn( name = "idAsset" ) )
    private Collection<Asset> asset;

    @ManyToMany
    @JoinTable( name = "T_Location_Team_Associations",
            joinColumns = @JoinColumn( name = "idLocation" ),
            inverseJoinColumns = @JoinColumn( name = "idTeam" ) )
    private Collection<Location> locations;
}
