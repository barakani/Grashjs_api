package com.grash.model.abstracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.Company;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Data
public class CompanyAudit extends Audit {
    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;
}
