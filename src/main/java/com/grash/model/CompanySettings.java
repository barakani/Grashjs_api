package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class CompanySettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private GeneralPreferences generalPreferences;

    @OneToOne
    private WorkOrderConfiguration workOrderConfiguration;

    @OneToOne
    private WorkRequestConfiguration workRequestConfiguration;

//    @OneToOne
//   private AssetFieldsConfiguration assetFieldsConfiguration;

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Role> roleList;

}
