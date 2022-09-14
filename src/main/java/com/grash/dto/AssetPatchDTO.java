package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.AssetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AssetPatchDTO {
    private boolean archived;

    private Image image;

    private Location location;

    private Asset parentAsset;

    private String area;

    private String barCode;

    private AssetCategory category;

    private String description;

    private User primaryUser;

    private Deprecation deprecation;

    private Date warrantyExpirationDate;

    private String additionalInfos;

    private AssetStatus status = AssetStatus.OPERATIONAL;

    private int uptime;

    private int downtime;

}
