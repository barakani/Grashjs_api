package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workOrderConfiguration", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<SingleWorkOrderFieldConfiguration> workOrderFieldConfigurations = createSingleWorkOrderFieldConfigurations(Arrays.asList("description",
            "priority", "images", "assigned", "additionalAssigned", "team", "asset"), Arrays.asList("files", "tasks", "time", "parts", "cost"));

    @OneToOne
    @JsonIgnore
    private CompanySettings companySettings;

    public WorkOrderConfiguration(CompanySettings companySettings) {
        this.companySettings = companySettings;
    }

    private Collection<SingleWorkOrderFieldConfiguration> createSingleWorkOrderFieldConfigurations(List<String> namesForCreation,
                                                                                                   List<String> namesForCompletion) {
        return Stream.concat(namesForCreation.stream().map(name ->
                                new SingleWorkOrderFieldConfiguration(name, true, this)),
                        namesForCompletion.stream().map(name ->
                                new SingleWorkOrderFieldConfiguration(name, false, this))).
                collect(Collectors.toList());
    }
}
