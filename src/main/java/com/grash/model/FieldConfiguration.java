package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.FieldType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"workOrderConfiguration", "WorkOrderRequestConfiguration"})

public class FieldConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String fieldName;

    private FieldType fieldType = FieldType.OPTIONAL;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WorkOrderRequestConfiguration WorkOrderRequestConfiguration;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WorkOrderConfiguration workOrderConfiguration;

    public FieldConfiguration(String fieldName, WorkOrderRequestConfiguration workOrderRequestConfiguration, WorkOrderConfiguration workOrderConfiguration
    ) {
        this.fieldName = fieldName;
        this.workOrderConfiguration = workOrderConfiguration;
        this.WorkOrderRequestConfiguration = workOrderRequestConfiguration;
    }

    public FieldConfiguration(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Collection<FieldConfiguration> createFieldConfigurations(List<String> fieldNames, WorkOrderRequestConfiguration workOrderRequestConfiguration, WorkOrderConfiguration workOrderConfiguration) {
        return fieldNames.stream().map(fieldName -> new FieldConfiguration(fieldName, workOrderRequestConfiguration, workOrderConfiguration)).collect(Collectors.toList());
    }
}
