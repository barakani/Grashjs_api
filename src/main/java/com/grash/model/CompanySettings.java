package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

//    private List<Role> roleList;

}
