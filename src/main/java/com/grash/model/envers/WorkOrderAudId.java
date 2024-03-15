package com.grash.model.envers;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class WorkOrderAudId implements Serializable {

    @Column(name = "id")
    private Long id;

    @Column(name = "rev")
    private Integer rev;

}
