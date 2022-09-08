package com.graphjs.model;
import com.graphjs.model.enums.BusinessType;
import com.graphjs.model.enums.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class GeneralPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private Language language;

    private String dateFormat;

//    private Currency currency;

//    private BusinessType businessType;

    private String timeZone;

    private String autoAssignWorkOrders;

    private String autoAssignRequests;

    private boolean disableWorkOrdersNotif;

    private boolean askFeedBackOnWOClosed;

    private String labelCostInTotalCost;

    private String wOUpdateForRequesters;


}
