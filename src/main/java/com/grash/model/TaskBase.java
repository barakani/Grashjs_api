package com.grash.model;

import com.grash.model.abstracts.Audit;
import com.grash.model.abstracts.Cost;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class TaskBase extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
}
