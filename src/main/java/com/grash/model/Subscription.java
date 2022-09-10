package com.grash.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int users;

    private boolean monthly;

    @OneToOne
    private SubscriptionPlan subscriptionPlan;
}
