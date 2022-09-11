package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.TaskBase;
import com.grash.model.enums.Status;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Task extends TaskBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Status status;

    private String note;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Image> images;

    @ManyToOne
    @NotNull
    private WorkOrder workOrder;
}