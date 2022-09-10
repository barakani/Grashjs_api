package com.grash.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double cost;

//  AssignedTo:List<User>

    private String barcode;

    private String description;

    private int quantity;

    private double area;

    @OneToOne
    private Location location;

    private Date createdAt;

//  private List<File>;

    @OneToOne
    private Image image;

//    assignedCustomers: list<Customer>

    private int minQuantity;

//    assignedTeams: list<Team>;

    @ManyToOne
    @NotNull
    private Asset asset;
}
