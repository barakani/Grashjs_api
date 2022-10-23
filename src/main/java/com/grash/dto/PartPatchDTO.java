package com.grash.dto;

import com.grash.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class PartPatchDTO {

    private String name;

    private double cost;

    private String category;

    private boolean nonStock;

    private String barcode;

    private String description;

    private int quantity;

    private String additionalInfos;

    private String area;

    private int minQuantity;

    private Location location;

    private Image image;

    private Asset asset;

    private Collection<User> assignedTo;

    private Collection<File> files;

    private Collection<Customer> customers;

    private Collection<Team> teams;
}
