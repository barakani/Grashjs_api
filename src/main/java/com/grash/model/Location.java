package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.CompanyAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Location extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String address;

    private String gps;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Location_Workers_Associations",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "id_worker"),
            indexes = {
                    @Index(name = "idx_location_worker_location_id", columnList = "id_location"),
                    @Index(name = "idx_location_worker_worker_id", columnList = "id_worker")
            })
    private Collection<User> workers;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Location_Team_Associations",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "id_team"),
            indexes = {
                    @Index(name = "idx_location_team_location_id", columnList = "id_location"),
                    @Index(name = "idx_location_team_team_id", columnList = "id_team")
            })
    private Collection<Team> teamList;

    @ManyToOne
    private Location parentLocation;

    @ManyToOne
    private Vendor vendor;

    @ManyToOne
    private Customer customer;
}

