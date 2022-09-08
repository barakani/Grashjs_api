package com.graphjs.model.abstracts;

import com.graphjs.model.enums.FieldType;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class FieldConfiguration {
    private String fieldName;
    private FieldType fieldType;
}
