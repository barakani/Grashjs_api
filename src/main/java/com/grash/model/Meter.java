package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Meter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
    private Collection<Reading> readingList;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    @NotNull
    private String name;

    private String unit;

    @NotNull
    private int updateFrequency;

    @OneToOne
    private MeterCategory meterCategory;

    @OneToOne
    private Image image;

    @ManyToMany
    @JoinTable(name = "T_Meter_User_Associations",
            joinColumns = @JoinColumn(name = "idMeter"),
            inverseJoinColumns = @JoinColumn(name = "idUser"))
    private Collection<User> users;

    @OneToOne
    private Location location;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Asset asset;

    @OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
    private Collection<WorkOrderMeterTrigger> workOrderMeterTriggerList;

}
