package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
    private Collection<Role> roleList = Arrays.asList(
            createRole("Administrator", Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Limited Administrator", Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Technician", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Limited technician", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("View only", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES)),
            createRole("Requester", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES))
    );

    public CompanySettings(Company company) {
        this.company = company;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<CostCategory> costCategories = createCostCategories(Arrays.asList("Drive cost", "Vendor cost", "Other cost", "Inspection cost", "Wrench cost"));

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<AssetCategory> assetCategories;

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<MeterCategory> meterCategories;

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<PurchaseOrderCategory> purchaseOrderCategories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<TimeCategory> timeCategories = createTimeCategories(Arrays.asList("Drive time", "Vendor time", "Other time", "Inspection time", "Wrench time"));

    @OneToMany(mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Checklist> checklists;

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
