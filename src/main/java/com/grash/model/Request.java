package com.grash.model;

import com.grash.model.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private Priority priority;

    @ManyToMany
    @JoinTable( name = "T_Request_File_Associations",
            joinColumns = @JoinColumn( name = "idRequest" ),
            inverseJoinColumns = @JoinColumn( name = "idFile" ) )
    private Collection<File> files;
    @OneToOne
    private Image image;

    private Long createdBy;

    //TODO
//    private Enum<OPTIONALFIELDS> optionalfields;


}
