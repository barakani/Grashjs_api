package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class MultiParts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable( name = "T_MultiPart_Part_Associations",
            joinColumns = @JoinColumn( name = "idMultiPart" ),
            inverseJoinColumns = @JoinColumn( name = "idPart" ) )
    private Collection<Part> partList;

    private String name;

}
