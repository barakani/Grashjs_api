package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class MultiParts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Company company;

    @ManyToMany
    @JoinTable(name = "T_MultiPart_Part_Associations",
            joinColumns = @JoinColumn(name = "idMultiPart"),
            inverseJoinColumns = @JoinColumn(name = "idPart"))
    private Collection<Part> partList;

    @NotNull
    private String name;

}
