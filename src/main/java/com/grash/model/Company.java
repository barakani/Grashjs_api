package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.BasicInfos;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Company extends BasicInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Image logo;

    private String city;

    private String state;

    private String zipCode;

    @OneToOne
    private Subscription subscription;

    @OneToOne(cascade = CascadeType.ALL)
    private CompanySettings companySettings = new CompanySettings(this);

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Customer> customers;
    
    @OneToOne
    @JsonIgnore
    private BankCard bankCard;
}
