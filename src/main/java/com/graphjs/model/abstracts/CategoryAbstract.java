package com.graphjs.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class CategoryAbstract extends Audit {
    private String name;
}
