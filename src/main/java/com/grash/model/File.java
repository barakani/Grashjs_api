package com.grash.model;

import com.grash.model.abstracts.FileAbstract;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class File extends FileAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(name = "T_Asset_File_Associations",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idAsset"))
    private Collection<Asset> asset;

    @ManyToMany
    @JoinTable(name = "T_Part_File_Associations",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idPart"))
    private Collection<Part> parts;

    @ManyToMany
    @JoinTable(name = "T_Request_File_Associations",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idRequest"))
    private Collection<Request> Requests;

    @ManyToMany
    @JoinTable(name = "T_WorkOrder_File_Associations",
            joinColumns = @JoinColumn(name = "idFile"),
            inverseJoinColumns = @JoinColumn(name = "idWorkOrder"))
    private Collection<WorkOrder> workOrders;


}
