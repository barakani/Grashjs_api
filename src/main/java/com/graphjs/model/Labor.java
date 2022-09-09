package com.graphjs.model;
import com.graphjs.model.abstracts.Cost;
import com.graphjs.model.abstracts.Time;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Labor extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // private User worker;

   // private LaborCost laborCost;
}
