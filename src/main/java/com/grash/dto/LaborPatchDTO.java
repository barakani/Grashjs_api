package com.grash.dto;

import com.grash.model.OwnUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LaborPatchDTO {
    private OwnUser worker;

    private int duration;

}
