package com.grash.dto;

import com.grash.model.PreventiveMaintenance;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SchedulePatchDTO {
    private String name;

    private Date startsOn;

    private int frequency;

    private Date endsOn;

    private PreventiveMaintenance preventiveMaintenance;
}
