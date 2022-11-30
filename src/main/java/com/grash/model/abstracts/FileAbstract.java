package com.grash.model.abstracts;

import com.grash.model.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class FileAbstract extends CompanyAudit {

    @NotNull
    private String name;

    @NotNull
    private String url;

    public FileAbstract(String name, String url, Company company) {
        this.name = name;
        this.url = url;
        this.setCompany(company);
    }
}
