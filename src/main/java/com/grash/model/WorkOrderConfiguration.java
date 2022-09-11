package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "workOrderConfiguration", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<SingleWorkOrderFieldConfiguration> workOrderFieldConfigurations;

    @OneToOne
    @JsonIgnore
    private CompanySettings companySettings;

    public WorkOrderConfiguration(CompanySettings companySettings){
        this.companySettings = companySettings;
    }
}
