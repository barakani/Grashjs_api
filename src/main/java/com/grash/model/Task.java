package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Task extends TaskBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Status status = Status.OPEN;

    private String note;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private WorkOrder workOrder;
}
