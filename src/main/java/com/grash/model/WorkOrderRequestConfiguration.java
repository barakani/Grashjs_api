package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

import static com.grash.model.FieldConfiguration.createFieldConfigurations;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderRequestConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "WorkOrderRequestConfiguration", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<FieldConfiguration> fieldConfigurations = createFieldConfigurations(Arrays.asList("asset", "location", "worker", "dueDate", "category", "team"), this);

    @OneToOne
    @JsonIgnore
    private CompanySettings companySettings;

    public WorkOrderRequestConfiguration(CompanySettings companySettings) {
        this.companySettings = companySettings;
    }

}
