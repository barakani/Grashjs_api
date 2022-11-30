package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.AssetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AssetShowDTO extends AuditShowDTO {
    private Long id;

    private boolean archived;

    private boolean hasChildren;

    private String description;

    private File image;

    private Location location;

    private Asset parentAsset;

    private String area;

    private String barCode;

    private AssetCategory category;

    private String name;

    private OwnUser primaryUser;

    private List<UserMiniDTO> assignedTo = new ArrayList<>();

    private List<TeamMiniDTO> teams = new ArrayList<>();

    private List<VendorMiniDTO> vendors = new ArrayList<>();

    private List<CustomerMiniDTO> customers = new ArrayList<>();

    private Deprecation deprecation;

    private Date warrantyExpirationDate;

    private Date inServiceDate;

    private String additionalInfos;

    private String serialNumber;

    private String model;

    private AssetStatus status = AssetStatus.OPERATIONAL;

    private int uptime;

    private int downtime;

    private List<FileMiniDTO> files = new ArrayList<>();

    private List<PartMiniDTO> parts = new ArrayList<>();

}
