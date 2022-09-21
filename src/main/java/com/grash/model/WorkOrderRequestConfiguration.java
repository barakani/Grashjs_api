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
public class WorkOrderRequestConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "WorkOrderRequestConfiguration", fetch = FetchType.LAZY)
    private Collection<FieldConfiguration> fieldConfigurations = new HashSet<>(createFieldConfigurations(Arrays.asList("asset", "location", "worker", "dueDate", "category", "team"), this, null));

    @OneToOne
    @JsonIgnore
    private CompanySettings companySettings;

    public WorkOrderRequestConfiguration(CompanySettings companySettings) {
        this.companySettings = companySettings;
    }

}
