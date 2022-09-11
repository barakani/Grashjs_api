package com.grash.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
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

    @ManyToMany
    @JoinTable( name = "T_Part_User_Associations",
            joinColumns = @JoinColumn( name = "idPart" ),
            inverseJoinColumns = @JoinColumn( name = "idUser" ) )
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
    @JoinTable( name = "T_Part_File_Associations",
            joinColumns = @JoinColumn( name = "idPart" ),
            inverseJoinColumns = @JoinColumn( name = "idFile" ) )
    private Collection<File> files;

    @OneToOne
    private Image image;

    @ManyToMany
    @JoinTable( name = "T_Part_Customer_Associations",
            joinColumns = @JoinColumn( name = "idPart" ),
            inverseJoinColumns = @JoinColumn( name = "idCustomer" ) )
    private Collection<Customer> assignedCustomer;

    private int minQuantity;

    @ManyToMany
    @JoinTable( name = "T_Part_Team_Associations",
            joinColumns = @JoinColumn( name = "idPart" ),
            inverseJoinColumns = @JoinColumn( name = "idTeam" ) )
    private Collection<Team> teams;

    @ManyToOne
    @NotNull
    private Asset asset;

    @ManyToMany
    @JoinTable( name = "T_MultiPart_Part_Associations",
            joinColumns = @JoinColumn( name = "idPart" ),
            inverseJoinColumns = @JoinColumn( name = "idMultiPart" ) )
    private Collection<MultiParts> multiParts;
}