package com.grash.model;

import com.grash.model.abstracts.BasicInfos;
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

    @ManyToMany
    @JoinTable(name = "T_Asset_Vendor_Associations",
            joinColumns = @JoinColumn(name = "id_vendor"),
            inverseJoinColumns = @JoinColumn(name = "id_asset"),
            indexes = {
                    @Index(name = "idx_vendor_asset_vendor_id", columnList = "id_vendor"),
                    @Index(name = "idx_vendor_asset_asset_id", columnList = "id_asset")
            })
    private Collection<Asset> asset;

}
