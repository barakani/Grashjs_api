package com.graphjs.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BasicInfos {
    private String name;
    private String address;
    private String phone;
    private String website;
    private String email;
}
