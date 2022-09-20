package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private double rate;

    @Column(unique = true)
    @NotNull
    private String email;

    private String phone;

    @ManyToOne
    @NotNull
    private Role role;

    private String jobTitle;

    @NotNull
    private String username;

    @JsonIgnore
    @NotNull
    private String password;

    private boolean enabled;

    @ManyToOne
    private Company company;

    private boolean ownsCompany;

    @OneToOne(cascade = CascadeType.ALL)
    private UserSettings userSettings = new UserSettings();

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Asset_User_Associations",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_asset"),
            indexes = {
                    @Index(name = "idx_user_asset_user_id", columnList = "id_user"),
                    @Index(name = "idx_user_asset_asset_id", columnList = "id_asset")
            })
    private Set<Asset> asset;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Location_User_Associations",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_location"),
            indexes = {
                    @Index(name = "idx_user_location_user_id", columnList = "id_user"),
                    @Index(name = "idx_user_location_location_id", columnList = "id_location")
            })
    private Set<Location> locations;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Meter_User_Associations",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_meter"),
            indexes = {
                    @Index(name = "idx_user_meter_user_id", columnList = "id_user"),
                    @Index(name = "idx_user_meter_meter_id", columnList = "id_meter")
            })
    private Set<Meter> meters;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Part_User_Associations",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_part"),
            indexes = {
                    @Index(name = "idx_user_part_user_id", columnList = "id_user"),
                    @Index(name = "idx_user_part_part_id", columnList = "id_part")
            })
    private Set<Part> parts;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Team_User_Associations",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_team"),
            indexes = {
                    @Index(name = "idx_user_team_user_id", columnList = "id_user"),
                    @Index(name = "idx_user_team_team_id", columnList = "id_team")
            })
    private Set<Team> teams;

    @ManyToMany
    @JsonIgnore
    private Set<PreventiveMaintenance> preventiveMaintenances;

    @ManyToMany
    @JsonIgnore
    private Set<WorkOrder> workOrders;


}

