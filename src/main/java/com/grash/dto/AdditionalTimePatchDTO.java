package com.grash.dto;

import com.grash.model.TimeCategory;
import com.grash.model.OwnUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AdditionalTimePatchDTO {
    private OwnUser assignedTo;

    private boolean includeToTotalTime;

    private double hourlyRate;

    private int duration;

    private Date startedAt;
    private TimeCategory timeCategory;
}
