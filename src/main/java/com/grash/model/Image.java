package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.abstracts.FileAbstract;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Image extends FileAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Task task;
}
