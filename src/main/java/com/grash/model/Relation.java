package com.grash.model;

import com.grash.model.abstracts.CompanyAudit;
import com.grash.model.enums.RelationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Relation extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private RelationType relationType = RelationType.RELATED_TO;

    @ManyToOne
    @NotNull
    private WorkOrder parent;

    @NotNull
    @ManyToOne
    private WorkOrder child;


}
