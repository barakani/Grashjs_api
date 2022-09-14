package com.grash.dto;

import com.grash.model.Image;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FloorPlanPatchDTO {

    private String name;

    private Image image;

    private double area;
}
