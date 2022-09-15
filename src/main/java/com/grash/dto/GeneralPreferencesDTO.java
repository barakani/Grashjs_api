package com.grash.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.CompanySettings;
import com.grash.model.Currency;
import com.grash.model.enums.BusinessType;
import com.grash.model.enums.DateFormat;
import com.grash.model.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralPreferencesDTO {

    private Language language;
    private Currency currency;
    private BusinessType businessType;
    private String timeZone;
    private boolean autoAssignWorkOrders;
    private boolean autoAssignRequests;
    private boolean disableClosedWorkOrdersNotif;
    private boolean askFeedBackOnWOClosed = true;
    private boolean laborCostInTotalCost = true;
    private boolean wOUpdateForRequesters = true;


}
