package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static com.grash.model.FieldConfiguration.createFieldConfigurations;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "companySettings")
public class WorkOrderConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workOrderConfiguration", fetch = FetchType.LAZY)
    private Collection<FieldConfiguration> workOrderFieldConfigurations = new HashSet<>(createFieldConfigurations(Arrays.asList("description",
            "priority", "images", "assigned", "additionalAssigned", "team", "asset", "files", "tasks", "time", "parts", "cost"), null, this));

    @OneToOne
    @JsonIgnore
    private CompanySettings companySettings;

    public WorkOrderConfiguration(CompanySettings companySettings) {
        this.companySettings = companySettings;
    }

}
