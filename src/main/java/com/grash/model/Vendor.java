package com.grash.model;

import com.grash.model.abstracts.BasicInfos;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Vendor extends BasicInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String vendorType;

    private String description;

    private double rate;

    @OneToOne
    private CustomField customField;

    @ManyToOne
    @NotNull
    private Asset asset;


}
