package com.grash.model;

import com.grash.model.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

//    private List<File> fileList;

    @OneToOne
    private Image image;

    private Long createdBy;

    //TODO
//    private Enum<OPTIONALFIELDS> optionalfields;


}
