package com.grash.model;

import com.sun.istack.NotNull;
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

}

