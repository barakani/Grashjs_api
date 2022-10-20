package com.grash.dto;

import com.grash.model.Company;
import com.grash.model.Location;
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

    private List<TeamShowDTO> teams = new ArrayList<>();

    private Location parentLocation;

    private List<VendorShowDTO> vendors = new ArrayList<>();

    private List<CustomerShowDTO> customers = new ArrayList<>();

    private List<UserShowDTO> workers = new ArrayList<>();
}
