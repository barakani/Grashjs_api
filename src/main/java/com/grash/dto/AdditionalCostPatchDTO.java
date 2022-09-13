package com.grash.dto;

import com.grash.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdditionalCostPatchDTO {
    private String description;
    private User assignedTo;
    private double cost;
}
