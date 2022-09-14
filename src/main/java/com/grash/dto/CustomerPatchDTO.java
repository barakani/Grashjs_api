package com.grash.dto;

import com.grash.model.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerPatchDTO {
    private String vendorType;

    private String description;

    private double rate;


    private String billingName;

    private String billingAddress;

    private String billingAddress2;

    private Currency billingCurrency;
}
