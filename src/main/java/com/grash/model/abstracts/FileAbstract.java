package com.grash.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class FileAbstract extends Audit {

    @NotNull
    private String name;

    @NotNull
    private String url;
}
