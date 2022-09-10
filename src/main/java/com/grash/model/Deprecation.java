package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Deprecation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double purchasePrice;

    private Date purchaseDate;

    private String residualValue;

    private String usefulLIfe;

    private int rate;

    private double currentValue;
}
