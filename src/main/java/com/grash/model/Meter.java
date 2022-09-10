package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String name;

    private String unit;

    private int updateFrequency;

    @OneToOne
    private MeterCategory meterCategory;

    @OneToOne
    private Image image;

    @ManyToMany
    @JoinTable( name = "T_Meter_User_Associations",
            joinColumns = @JoinColumn( name = "idMeter" ),
            inverseJoinColumns = @JoinColumn( name = "idUser" ) )
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
