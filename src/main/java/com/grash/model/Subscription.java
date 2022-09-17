package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private int usersCount;

    private boolean monthly;

    @OneToOne
    @NotNull
    private SubscriptionPlan subscriptionPlan;

    @OneToOne
    @NotNull
    private Company company;
}
