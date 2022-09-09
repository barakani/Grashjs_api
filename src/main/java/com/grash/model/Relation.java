package com.grash.model;

import com.grash.model.abstracts.WorkOrderBase;
import com.grash.model.enums.RelationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private RelationType relationType;

    //private WorkOrder parent;

   // private WorkOrder child;


}
