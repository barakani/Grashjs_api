package com.grash.model;

import com.grash.model.abstracts.BasicInfos;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Customer extends BasicInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String vendorType;

    private String description;

    private double rate;


    private String billingName;

    private String billingAddress;

    private String billingAddress2;

    @OneToOne
    private Currency billingCurrency;

    @ManyToMany
    @JoinTable(name = "T_Part_customer_Associations",
            joinColumns = @JoinColumn(name = "id_customer"),
            inverseJoinColumns = @JoinColumn(name = "id_part"),
            indexes = {
                    @Index(name = "idx_customer_part_customer_id", columnList = "id_customer"),
                    @Index(name = "idx_customer_part_part_id", columnList = "id_part")
            })
    private Collection<Part> parts;

}
