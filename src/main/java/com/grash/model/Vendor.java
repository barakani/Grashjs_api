package com.grash.model;

import com.grash.model.abstracts.BasicInfos;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

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

    @ManyToMany
    @JoinTable( name = "T_Asset_Vendor_Associations",
            joinColumns = @JoinColumn( name = "idVendor" ),
            inverseJoinColumns = @JoinColumn( name = "idAsset" ) )
    private Collection<Asset> asset;


}
