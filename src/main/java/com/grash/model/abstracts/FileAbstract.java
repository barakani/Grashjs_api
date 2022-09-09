package com.grash.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class FileAbstract extends Audit {
    private String name;

    private String url;
}
