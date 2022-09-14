package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.Audit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Location extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String address;

    private String gps;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Asset> assetList;

    @ManyToMany
    @JoinTable(name = "T_Location_Workers_Associations",
            joinColumns = @JoinColumn(name = "idLocation"),
            inverseJoinColumns = @JoinColumn(name = "idWorker"))
    private Collection<User> workers;

    @ManyToMany
    @JoinTable(name = "T_Location_Team_Associations",
            joinColumns = @JoinColumn(name = "idLocation"),
            inverseJoinColumns = @JoinColumn(name = "idTeam"))
    private Collection<Team> teamList;

    @OneToOne
    private Location parentLocation;

    @OneToOne
    private Vendor vendor;

    @OneToOne
    private Customer customer;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Part> partList;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<FloorPlan> floorPlanList;
}

