package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class WorkRequestConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "workRequestConfiguration", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<FieldConfiguration> fieldConfigurations;

    @OneToOne
    @JsonIgnore
    private CompanySettings companySettings;

    public WorkRequestConfiguration(CompanySettings companySettings){
        this.companySettings = companySettings;
    }
}
