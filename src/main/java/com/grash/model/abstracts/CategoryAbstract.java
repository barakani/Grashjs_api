package com.grash.model.abstracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.CompanySettings;
import com.grash.model.OwnUser;
import com.grash.security.CustomUserDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class CategoryAbstract extends Audit {

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CompanySettings companySettings;

    @PrePersist
    public void beforePersist() {
        OwnUser user = ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        CompanySettings companySettings = user.getCompany().getCompanySettings();
        this.setCompanySettings(companySettings);
    }

    public CategoryAbstract(String name) {
        this.name = name;
    }

}
