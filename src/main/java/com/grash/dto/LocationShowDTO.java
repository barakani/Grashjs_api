package com.grash.dto;

import com.grash.model.Company;
import com.grash.model.Location;
import com.grash.model.Team;
import com.grash.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LocationShowDTO {
    private Company company;

    private Long id;

    private String name;

    private String address;

    private Double longitude;

    private Double latitude;

    private List<User> workers = new ArrayList<>();

    private List<Team> teams = new ArrayList<>();

    private Location parentLocation;

    private List<VendorShowDTO> vendors = new ArrayList<>();

    private List<CustomerShowDTO> customers = new ArrayList<>();
}
