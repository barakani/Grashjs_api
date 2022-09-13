package com.grash.dto;

import com.grash.model.TimeCategory;
import com.grash.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AdditionalTimePatchDTO {
    private User assignedTo;

    private boolean includeToTotalTime;

    private double hourlyRate;

    private Date startedAt;
    private TimeCategory timeCategory;
}
