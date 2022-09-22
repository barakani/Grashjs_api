package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private List<TaskBase> taskBases = new ArrayList<>();

    @NotNull
    private String name;

    private String description;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CompanySettings companySettings;

}
