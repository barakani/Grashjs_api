package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

//    @OneToOne
//   private AssetFieldsConfiguration assetFieldsConfiguration;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Role> roleList = new HashSet<>(Arrays.asList(
            createRole("Administrator", Arrays.asList(BasicPermission.CREATE_EDIT_PEOPLE_AND_TEAMS, BasicPermission.CREATE_EDIT_CATEGORIES, BasicPermission.DELETE_WORK_ORDERS, BasicPermission.DELETE_PREVENTIVE_MAINTENANCE_TRIGGERS, BasicPermission.DELETE_ASSETS, BasicPermission.DELETE_PARTS_AND_MULTI_PARTS, BasicPermission.DELETE_PURCHASE_ORDERS, BasicPermission.DELETE_METERS, BasicPermission.DELETE_VENDORS_AND_CUSTOMERS, BasicPermission.DELETE_CATEGORIES, BasicPermission.DELETE_FILES, BasicPermission.DELETE_PEOPLE_AND_TEAMS, BasicPermission.ACCESS_SETTINGS)),
            createRole("Limited Administrator", Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Technician", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Limited technician", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("View only", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Requester", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES))));

    public CompanySettings(Company company) {
        this.company = company;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CostCategory> costCategories = new HashSet<>(createCostCategories(Arrays.asList("Drive cost", "Vendor cost", "Other cost", "Inspection cost", "Wrench cost")));

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TimeCategory> timeCategories = new HashSet<>(createTimeCategories(Arrays.asList("Drive time", "Vendor time", "Other time", "Inspection time", "Wrench time")));

    private Role createRole(String name, List<BasicPermission> basicPermissions) {
        return new Role(RoleType.ROLE_CLIENT, name, new HashSet<>(basicPermissions), this);
    }

    private List<CostCategory> createCostCategories(List<String> costCategories) {
        return costCategories.stream().map(costCategory -> new CostCategory(costCategory, this)).collect(Collectors.toList());
    }

    private List<TimeCategory> createTimeCategories(List<String> timeCategories) {
        return timeCategories.stream().map(timeCategory -> new TimeCategory(timeCategory, this)).collect(Collectors.toList());
    }
}
