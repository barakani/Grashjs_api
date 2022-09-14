package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.enums.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class FieldConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String fieldName;

    private FieldType fieldType = FieldType.OPTIONAL;

    @ManyToOne
    @JsonIgnore
    private WorkOrderRequestConfiguration WorkOrderRequestConfiguration;

    public FieldConfiguration(String fieldName, WorkOrderRequestConfiguration workOrderRequestConfiguration) {
        this.fieldName = fieldName;
        this.WorkOrderRequestConfiguration = workOrderRequestConfiguration;
    }

    public FieldConfiguration(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Collection<FieldConfiguration> createFieldConfigurations(List<String> fieldNames, WorkOrderRequestConfiguration workOrderRequestConfiguration) {
        return fieldNames.stream().map(fieldName -> new FieldConfiguration(fieldName, workOrderRequestConfiguration)).collect(Collectors.toList());
    }
}
