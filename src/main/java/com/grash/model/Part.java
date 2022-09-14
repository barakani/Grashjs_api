package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private double cost;

    @ManyToOne
    @NotNull
    private Company company;

    @ManyToMany
    @JoinTable(name = "T_Part_User_Associations",
            joinColumns = @JoinColumn(name = "idPart"),
            inverseJoinColumns = @JoinColumn(name = "idUser"))
    private Collection<User> assignedTo;

    private String barcode;

    private String description;

    private int quantity;

    private double area;

    @ManyToOne
    @NotNull
    private Location location;

    private Date createdAt;

    @ManyToMany
    @JoinTable(name = "T_Part_File_Associations",
            joinColumns = @JoinColumn(name = "idPart"),
            inverseJoinColumns = @JoinColumn(name = "idFile"))
    private Collection<File> files;

    @OneToOne
    private Image image;

    @ManyToMany
    @JoinTable(name = "T_Part_Customer_Associations",
            joinColumns = @JoinColumn(name = "idPart"),
            inverseJoinColumns = @JoinColumn(name = "idCustomer"))
    private Collection<Customer> assignedCustomer;

    private int minQuantity;

    @ManyToMany
    @JoinTable(name = "T_Part_Team_Associations",
            joinColumns = @JoinColumn(name = "idPart"),
            inverseJoinColumns = @JoinColumn(name = "idTeam"))
    private Collection<Team> teams;

    @ManyToOne
    @NotNull
    private Asset asset;

    @ManyToMany
    @JoinTable(name = "T_MultiPart_Part_Associations",
            joinColumns = @JoinColumn(name = "idPart"),
            inverseJoinColumns = @JoinColumn(name = "idMultiPart"))
    private Collection<MultiParts> multiParts;
}
