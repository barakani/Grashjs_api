package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.abstracts.CompanyAudit;
import com.grash.model.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Request extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean approved;

    @NotNull
    private String title;

    private String description;

    private Priority priority = Priority.NONE;

    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(name = "T_Request_File_Associations",
            joinColumns = @JoinColumn(name = "id_request"),
            inverseJoinColumns = @JoinColumn(name = "id_file"),
            indexes = {
                    @Index(name = "idx_request_file_request_id", columnList = "id_request"),
                    @Index(name = "idx_request_file_file_id", columnList = "id_file")
            })
    private List<File> files = new ArrayList<>();
    @OneToOne
    private Image image;

    private Long createdBy;

//  optionalfields;

    @ManyToOne
    private Asset asset;

    @ManyToOne
    private Location location;

    @ManyToOne
    private OwnUser assignedTo;

    private Date dueDate;

    private String category;

    @ManyToOne
    private Team team;

}
