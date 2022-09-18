package com.grash.dto;

import com.grash.model.Customer;
import com.grash.model.Location;
import com.grash.model.Vendor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationPatchDTO {
    private String name;

    private String address;

    private String gps;

    private Location parentLocation;

    private Vendor vendor;

    private Customer customer;

}
