package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CompanySettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private GeneralPreferences generalPreferences = new GeneralPreferences(this);

    @OneToOne(cascade = CascadeType.ALL)
    private WorkOrderConfiguration workOrderConfiguration = new WorkOrderConfiguration(this);

    @OneToOne(cascade = CascadeType.ALL)
    private WorkOrderRequestConfiguration WorkOrderRequestConfiguration = new WorkOrderRequestConfiguration(this);

    @OneToOne
    @JsonIgnore
    private Company company;

//    @OneToOne
//   private AssetFieldsConfiguration assetFieldsConfiguration;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Role> roleList = Arrays.asList(
            createRole("Administrator",Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Limited Administrator",Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Technician",Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Limited technician",Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("View only",Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Requester",Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES))
            );

    private Role createRole(String name, List<BasicPermission> basicPermissions){
        return new Role(RoleType.ROLE_CLIENT, name, new HashSet<>(basicPermissions), this);
    }

    public CompanySettings(Company company){
        this.company = company;
    }

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<CostCategory> costCategories;

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<AssetCategory> assetCategories;

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<MeterCategory> meterCategories;

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<PurchaseOrderCategory> purchaseOrderCategories;

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<TimeCategory> timeCategories;

}
