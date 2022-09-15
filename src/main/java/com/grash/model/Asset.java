package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.abstracts.Audit;
import com.grash.model.enums.AssetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Asset extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createAt;

    private boolean archived;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    @OneToOne
    private Image image;

    @ManyToOne
    @NotNull
    private Location location;

    @OneToOne
    private Asset parentAsset;

    private String area;

    private String barCode;

    @OneToOne
    private AssetCategory category;

    private String description;

    @OneToOne
    private User primaryUser;

    @ManyToMany
    @JoinTable(name = "T_Asset_User_Associations",
            joinColumns = @JoinColumn(name = "id_asset"),
            inverseJoinColumns = @JoinColumn(name = "id_user"),
            indexes = {
                    @Index(name = "idx_asset_user_asset_id", columnList = "id_asset"),
                    @Index(name = "idx_asset_user_user_id", columnList = "id_user")
            })
    private Collection<User> assignedTo;

    @ManyToMany
    @JoinTable(name = "T_Asset_Team_Associations",
            joinColumns = @JoinColumn(name = "id_asset"),
            inverseJoinColumns = @JoinColumn(name = "id_team"),
            indexes = {
                    @Index(name = "idx_asset_team_asset_id", columnList = "id_asset"),
                    @Index(name = "idx_asset_team_team_id", columnList = "id_team")
            })
    private Collection<Team> teams;

    @ManyToMany
    @JoinTable(name = "T_Asset_Vendor_Associations",
            joinColumns = @JoinColumn(name = "id_asset"),
            inverseJoinColumns = @JoinColumn(name = "id_vendor"),
            indexes = {
                    @Index(name = "idx_asset_vendor_asset_id", columnList = "id_asset"),
                    @Index(name = "idx_asset_vendor_vendor_id", columnList = "id_vendor")
            })
    private Collection<Vendor> vendors;

    @OneToOne
    private Deprecation deprecation;

    private Date warrantyExpirationDate;

    private String additionalInfos;

    private AssetStatus status = AssetStatus.OPERATIONAL;

    private int uptime;

    private int downtime;

    @ManyToMany
    @JoinTable(name = "T_Asset_File_Associations",
            joinColumns = @JoinColumn(name = "id_asset"),
            inverseJoinColumns = @JoinColumn(name = "id_file"),
            indexes = {
                    @Index(name = "idx_asset_file_asset_id", columnList = "id_asset"),
                    @Index(name = "idx_asset_file_file_id", columnList = "id_file")
            })
    private Collection<File> files;

}



