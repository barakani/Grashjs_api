package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.FileAbstract;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class File extends FileAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Asset_File_Associations",
            joinColumns = @JoinColumn(name = "id_file"),
            inverseJoinColumns = @JoinColumn(name = "id_asset"),
            indexes = {
                    @Index(name = "idx_file_asset_file_id", columnList = "id_file"),
                    @Index(name = "idx_file_asset_asset_id", columnList = "id_asset")
            })
    private Set<Asset> asset;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Part_File_Associations",
            joinColumns = @JoinColumn(name = "id_file"),
            inverseJoinColumns = @JoinColumn(name = "id_part"),
            indexes = {
                    @Index(name = "idx_file_part_file_id", columnList = "id_file"),
                    @Index(name = "idx_file_part_part_id", columnList = "id_part")
            })
    private Set<Part> parts;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_Request_File_Associations",
            joinColumns = @JoinColumn(name = "id_file"),
            inverseJoinColumns = @JoinColumn(name = "id_request"),
            indexes = {
                    @Index(name = "idx_file_request_file_id", columnList = "id_file"),
                    @Index(name = "idx_file_request_request_id", columnList = "id_request")
            })
    private Set<Request> Requests;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "T_WorkOrder_File_Associations",
            joinColumns = @JoinColumn(name = "id_file"),
            inverseJoinColumns = @JoinColumn(name = "id_work_order"),
            indexes = {
                    @Index(name = "idx_file_work_order_file_id", columnList = "id_file"),
                    @Index(name = "idx_file_work_order_work_order_id", columnList = "id_work_order")
            })
    private Set<WorkOrder> workOrders;


}
