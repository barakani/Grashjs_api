package com.grash.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptionPlanPatchDTO {

    private double monthlyCostPerUser;

    private double yearlyCostPerUser;

    private String code;
}
