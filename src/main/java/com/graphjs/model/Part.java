package com.graphjs.model;

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
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double cost;

//  AssignedTo:List<User>

    private String barcode;

    private String description;

    private double quantity;

    private double area;

//  private Location location;

    private Date createdAt;

//  private List<File>;

//    private Image image;

//    assignedCustomers: list<Customer>

    private double minQuantity;

//    assignedTeams: list<Team>;
}
