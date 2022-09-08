package com.graphjs.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private List<TaskBase> taskBaseList;

    private String name;

    private String description;

}
