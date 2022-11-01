package com.grash.dto;

import com.grash.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class LocationPatchDTO {
    private String name;

    private String address;

    private Double longitude;

    private Double latitude;

    private Location parentLocation;

    private Collection<OwnUser> workers;

    private Collection<Team> teams;

    private Collection<Vendor> vendors;

    private Collection<Customer> customers;

}
