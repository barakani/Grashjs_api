package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    @ManyToMany
    @JoinTable(name = "T_Part_User_Associations",
            joinColumns = @JoinColumn(name = "id_part"),
            inverseJoinColumns = @JoinColumn(name = "id_user"),
            indexes = {
                    @Index(name = "idx_part_user_part_id", columnList = "id_part"),
                    @Index(name = "idx_part_user_user_id", columnList = "id_user")
            })
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
            joinColumns = @JoinColumn(name = "id_part"),
            inverseJoinColumns = @JoinColumn(name = "id_file"),
            indexes = {
                    @Index(name = "idx_part_file_part_id", columnList = "id_part"),
                    @Index(name = "idx_part_file_file_id", columnList = "id_file")
            })
    private Collection<File> files;

    @OneToOne
    private Image image;

    @ManyToMany
    @JoinTable(name = "T_Part_Customer_Associations",
            joinColumns = @JoinColumn(name = "id_part"),
            inverseJoinColumns = @JoinColumn(name = "id_customer"),
            indexes = {
                    @Index(name = "idx_part_customer_part_id", columnList = "id_part"),
                    @Index(name = "idx_part_customer_customer_id", columnList = "id_customer")
            })
    private Collection<Customer> assignedCustomer;

    private int minQuantity;

    @ManyToMany
    @JoinTable(name = "T_Part_Team_Associations",
            joinColumns = @JoinColumn(name = "id_part"),
            inverseJoinColumns = @JoinColumn(name = "id_team"),
            indexes = {
                    @Index(name = "idx_part_team_part_id", columnList = "id_part"),
                    @Index(name = "idx_part_team_team_id", columnList = "id_team")
            })
    private Collection<Team> teams;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Asset asset;

    @ManyToMany
    @JoinTable(name = "T_MultiParts_Part_Associations",
            joinColumns = @JoinColumn(name = "id_part"),
            inverseJoinColumns = @JoinColumn(name = "id_multi_parts"),
            indexes = {
                    @Index(name = "idx_part_multi_parts_part_id", columnList = "id_part"),
                    @Index(name = "idx_part_multi_parts_multi_parts_id", columnList = "id_multi_parts")
            })
    private Collection<MultiParts> multiParts;
}
