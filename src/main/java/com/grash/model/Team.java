package com.grash.model;

import com.grash.model.abstracts.CompanyAudit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Team extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(name = "T_Team_User_Associations",
            joinColumns = @JoinColumn(name = "id_team"),
            inverseJoinColumns = @JoinColumn(name = "id_user"),
            indexes = {
                    @Index(name = "idx_team_user_team_id", columnList = "id_team"),
                    @Index(name = "idx_team_user_user_id", columnList = "id_user")
            })
    private Collection<User> users;

    @NotNull
    private String name;

    private String description;

    @ManyToMany
    @JoinTable(name = "T_Team_Asset_Associations",
            joinColumns = @JoinColumn(name = "id_team"),
            inverseJoinColumns = @JoinColumn(name = "id_asset"),
            indexes = {
                    @Index(name = "idx_team_asset_team_id", columnList = "id_team"),
                    @Index(name = "idx_team_asset_asset_id", columnList = "id_asset")
            })
    private Collection<Asset> asset;

    @ManyToMany
    @JoinTable(name = "T_Location_Team_Associations",
            inverseJoinColumns = @JoinColumn(name = "id_team"),
            joinColumns = @JoinColumn(name = "id_location"),
            indexes = {
                    @Index(name = "idx_team_location_team_id", columnList = "id_team"),
                    @Index(name = "idx_team_location_location_id", columnList = "id_location")
            })
    private Collection<Location> locations;
}
