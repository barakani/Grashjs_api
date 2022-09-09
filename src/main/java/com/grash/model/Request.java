package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

//    private Enum<PRIORITY> priority;

//    private List<File> fileList;

//    private Image image;

    private Long createdBy;

//    private Enum<OPTIONALFIELDS> optionalfields;


}
