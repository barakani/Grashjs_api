package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private int number;

    @NotNull
    private String expirationMonth;

    @NotNull
    private String expirationYear;

    @NotNull
    private int cvv;

    @NotNull
    private String cardholderName;

}
