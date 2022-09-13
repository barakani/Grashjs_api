package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Collection<Reading> readingList;

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
    private Asset asset;

    @OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<WorkOrderMeterTrigger> workOrderMeterTriggerList;

}
