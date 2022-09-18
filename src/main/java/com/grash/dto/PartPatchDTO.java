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

    private String barcode;

    private String description;

    private int quantity;

    private double area;

    private int minQuantity;

    private Location location;

    private Image image;

    private Asset asset;

    private Company company;
}
