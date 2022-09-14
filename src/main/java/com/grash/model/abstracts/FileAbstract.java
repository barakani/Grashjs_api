package com.grash.model.abstracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.Company;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class FileAbstract extends Audit {

    @NotNull
    private String name;

    @NotNull
    private String url;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;
}
