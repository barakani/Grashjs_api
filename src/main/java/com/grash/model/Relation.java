package com.grash.model;

import com.grash.model.enums.RelationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private RelationType relationType = RelationType.RELATED_TO;

    @ManyToOne
    private WorkOrder parent;

    @ManyToOne
    private WorkOrder child;


}
