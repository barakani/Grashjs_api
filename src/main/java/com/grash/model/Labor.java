package com.grash.model;
import com.grash.model.abstracts.Time;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Labor extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User worker;

    @OneToOne
    private LaborCost laborCost;
}
