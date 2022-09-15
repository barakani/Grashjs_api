package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    private String description;

    private Priority priority = Priority.NONE;

    @ManyToMany
    @JoinTable(name = "T_Request_File_Associations",
            joinColumns = @JoinColumn(name = "id_request"),
            inverseJoinColumns = @JoinColumn(name = "id_file"),
            indexes = {
                    @Index(name = "idx_request_file_request_id", columnList = "id_request"),
                    @Index(name = "idx_request_file_file_id", columnList = "id_file")
            })
    private Collection<File> files;
    @OneToOne
    private Image image;

    private Long createdBy;

//  optionalfields;

    @OneToOne
    private Asset asset;

    @OneToOne
    private Location location;

    @OneToOne
    private User assignedTo;

    private Date dueDate;

    private String category;

    @OneToOne
    private Team team;

}
