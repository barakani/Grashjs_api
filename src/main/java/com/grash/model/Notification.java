package com.grash.model;

import com.grash.model.abstracts.Audit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Notification extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String message;

    private boolean isRead;

    @ManyToOne
    private User user;


    public Notification(String message, User user) {
        this.message = message;
        this.user = user;
    }

}
