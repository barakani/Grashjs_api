package com.grash.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PartQuantityPatchDTO {
    @NotNull
    private int quantity;
}
