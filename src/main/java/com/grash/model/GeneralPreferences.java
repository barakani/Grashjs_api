package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.enums.BusinessType;
import com.grash.model.enums.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class GeneralPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Language language = Language.EN;

    private String dateFormat;

    @OneToOne
    private Currency currency;

    private BusinessType businessType;

    private String timeZone;

    private boolean autoAssignWorkOrders;

    private boolean autoAssignRequests;

    private boolean disableClosedWorkOrdersNotif;

    private boolean askFeedBackOnWOClosed = true;

    private boolean laborCostInTotalCost = true;

    private boolean wOUpdateForRequesters = true;

    @OneToOne
    @JsonIgnore
    private CompanySettings companySettings;

    public GeneralPreferences(CompanySettings companySettings){
        this.companySettings = companySettings;
    }


}
