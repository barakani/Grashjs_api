package com.graphjs.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private Image logo;

    private String city;

    private String state;

    private String zipCode;

//    private SubscriptionPlan subscriptionPlan;

//    private CompanySettings companySettings;

}
