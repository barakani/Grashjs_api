package com.graphjs.model;
import com.graphjs.model.abstracts.TaskBase;
import com.graphjs.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Task extends TaskBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private Status status;

    private String note;

    // private Collection<Image> images;
}
