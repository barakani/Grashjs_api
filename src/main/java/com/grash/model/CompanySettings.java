package com.grash.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class CompanySettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private GeneralPreferences generalPreferences;

//    private WorkOrderConfiguration workOrderConfiguration;

//    private WorkRequestConfiguration workRequestConfiguration;

//    private FieldsConfiguration fieldsConfiguration;

//    private List<Role> roleList;

}
