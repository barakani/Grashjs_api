package com.grash.dto;

import com.grash.model.Company;
import com.grash.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
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

    private List<TeamMiniDTO> teams = new ArrayList<>();

    private Location parentLocation;

    private List<VendorMiniDTO> vendors = new ArrayList<>();

    private List<CustomerMiniDTO> customers = new ArrayList<>();

    private List<UserMiniDTO> workers = new ArrayList<>();

    private Long createdBy;

    private Long updatedBy;

    private Instant createdAt;

    private Instant updatedAt;
}
