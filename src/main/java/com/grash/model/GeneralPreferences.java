package com.grash.model;

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

    private String autoAssignWorkOrders;

    private String autoAssignRequests;

    private boolean disableWorkOrdersNotif;

    private boolean askFeedBackOnWOClosed;

    private String labelCostInTotalCost;

    private String wOUpdateForRequesters;


}
