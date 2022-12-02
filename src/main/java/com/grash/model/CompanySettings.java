package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.PermissionEntity;
import com.grash.model.enums.RoleCode;
import com.grash.model.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;
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
    private Set<Role> roleList = getDefaultRoles();

    public CompanySettings(Company company) {
        this.company = company;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CostCategory> costCategories = new ArrayList<>(createCostCategories(Arrays.asList("Drive cost", "Vendor cost", "Other cost", "Inspection cost", "Wrench cost")));

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companySettings", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TimeCategory> timeCategories = new ArrayList<>(createTimeCategories(Arrays.asList("Drive time", "Vendor time", "Other time", "Inspection time", "Wrench time")));

    private Role createRole(String name,
                            RoleCode code,
                            List<PermissionEntity> createPermissions,
                            List<PermissionEntity> editOtherPermissions,
                            List<PermissionEntity> deleteOtherPermissions,
                            List<PermissionEntity> viewOtherPermissions,
                            List<PermissionEntity> viewPermissions
    ) {
        return Role.builder()
                .roleType(RoleType.ROLE_CLIENT)
                .companySettings(this)
                .code(code)
                .name(name)
                .createPermissions(new HashSet<>(createPermissions))
                .editOtherPermissions(new HashSet<>(editOtherPermissions))
                .deleteOtherPermissions(new HashSet<>(deleteOtherPermissions))
                .viewOtherPermissions(new HashSet<>(viewOtherPermissions))
                .viewPermissions(new HashSet<>(viewPermissions))
                .build();

    }

    private List<CostCategory> createCostCategories(List<String> costCategories) {
        return costCategories.stream().map(costCategory -> new CostCategory(costCategory, this)).collect(Collectors.toList());
    }

    private List<TimeCategory> createTimeCategories(List<String> timeCategories) {
        return timeCategories.stream().map(timeCategory -> new TimeCategory(timeCategory, this)).collect(Collectors.toList());
    }

    private Set<Role> getDefaultRoles() {
        List<PermissionEntity> allEntities = Arrays.asList(PermissionEntity.PEOPLE_AND_TEAMS, PermissionEntity.CATEGORIES, PermissionEntity.WORK_ORDERS, PermissionEntity.PREVENTIVE_MAINTENANCES, PermissionEntity.ASSETS, PermissionEntity.PARTS_AND_MULTIPARTS, PermissionEntity.PURCHASE_ORDERS, PermissionEntity.METERS, PermissionEntity.VENDORS_AND_CUSTOMERS, PermissionEntity.FILES, PermissionEntity.LOCATIONS, PermissionEntity.SETTINGS, PermissionEntity.REQUESTS);
        return new HashSet<>(Arrays.asList(
                createRole("Administrator", RoleCode.ADMIN, allEntities, allEntities, allEntities, allEntities, allEntities),
                createRole("Limited Administrator", RoleCode.LIMITED_ADMIN, allEntities, allEntities, allEntities, allEntities, allEntities.stream().filter(permissionEntity -> permissionEntity != PermissionEntity.SETTINGS).collect(Collectors.toList())),
                createRole("Technician", RoleCode.TECHNICIAN, allEntities, allEntities, allEntities, allEntities, Arrays.asList(PermissionEntity.WORK_ORDERS, PermissionEntity.LOCATIONS, PermissionEntity.ASSETS)),
                createRole("Limited Technician", RoleCode.LIMITED_TECHNICIAN, allEntities, allEntities, allEntities, allEntities, Arrays.asList(PermissionEntity.WORK_ORDERS, PermissionEntity.LOCATIONS, PermissionEntity.ASSETS)),
                createRole("View Only", RoleCode.VIEW_ONLY, allEntities, allEntities, allEntities, allEntities, allEntities.stream().filter(permissionEntity -> permissionEntity != PermissionEntity.SETTINGS).collect(Collectors.toList())),
                createRole("Requester", RoleCode.REQUESTER, allEntities, allEntities, allEntities, allEntities, Collections.singletonList(PermissionEntity.REQUESTS))
        ));
//            createRole("Limited Administrator", Arrays.asList(BasicPermission.ACCESS_SETTINGS, BasicPermission.CREATE_EDIT_CATEGORIES), RoleCode.LIMITED_ADMIN),
//            createRole("Technician", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES), RoleCode.TECHNICIAN),
//            createRole("Limited technician", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES), RoleCode.LIMITED_TECHNICIAN),
//            createRole("View only", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES), RoleCode.VIEW_ONLY),
//            createRole("Requester", Arrays.asList(BasicPermission.CREATE_EDIT_CATEGORIES), RoleCode.REQUESTER)));

    }
}
