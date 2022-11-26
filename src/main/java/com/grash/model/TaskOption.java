package com.grash.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grash.model.abstracts.CompanyAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class TaskOption extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;

    @ManyToOne
    @JsonBackReference
    private TaskBase taskBase;

    public TaskOption(String label, Company company, TaskBase taskBase) {
        this.label = label;
        this.taskBase = taskBase;
        this.setCompany(company);
    }
}
