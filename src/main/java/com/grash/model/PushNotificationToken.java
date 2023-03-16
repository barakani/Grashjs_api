package com.grash.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PushNotificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @OneToOne(targetEntity = OwnUser.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private OwnUser user;
}
