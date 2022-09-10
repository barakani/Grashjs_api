package com.grash.model;
import com.grash.model.abstracts.BasicInfos;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @OneToOne
    private CompanySettings companySettings;

}
