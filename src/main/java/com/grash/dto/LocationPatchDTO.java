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

    private String gps;

    private Location parentLocation;

    private Vendor vendor;

    private Customer customer;

    private Collection<User> workers;

    private Collection<Team> teams;

}
