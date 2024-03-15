package com.grash.model.envers;

import com.grash.model.*;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

//@Entity
@Data
@Table(name = "work_order_aud")
public class WorkOrderAud {
    @EmbeddedId
    private WorkOrderAudId workOrderAudId;

    @Column(name = "revtype")
    private Integer revtype;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "estimatedDuration")
    private Integer estimatedDuration;

    @Column(name = "description")
    private String description;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "requiredSignature")
    private Boolean requiredSignature;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private File image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private WorkOrderCategory category;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "primary_user_id")
    private OwnUser primaryUser;

    @ManyToOne
    @JoinColumn(name = "completed_by_id")
    private OwnUser completedBy;

    @Column(name = "completed_on")
    private Date completedOn;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "signature_id")
    private File signature;

    @Column(name = "archived")
    private Boolean archived;

    @ManyToOne
    @JoinColumn(name = "parent_request_id")
    private Request parentRequest;

    @Column(name = "feedback")
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "parent_preventive_maintenance_id")
    private PreventiveMaintenance parentPreventiveMaintenance;

    // Include fields for _mod columns

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    // Include fields for _mod columns for asset_id
    // Fields for mod columns
    @Column(name = "dueDate_mod")
    private Boolean dueDateMod;

    @Column(name = "priority_mod")
    private Boolean priorityMod;

    @Column(name = "estimatedDuration_mod")
    private Boolean estimatedDurationMod;

    @Column(name = "description_mod")
    private Boolean descriptionMod;

    @Column(name = "title_mod")
    private Boolean titleMod;

    @Column(name = "requiredSignature_mod")
    private Boolean requiredSignatureMod;

    @Column(name = "image_id_mod")
    private Boolean imageIdMod;

    @Column(name = "category_id_mod")
    private Boolean categoryIdMod;

    @Column(name = "location_id_mod")
    private Boolean locationIdMod;

    @Column(name = "team_id_mod")
    private Boolean teamIdMod;

    @Column(name = "primary_user_id_mod")
    private Boolean primaryUserIdMod;

    @Column(name = "completed_by_id_mod")
    private Boolean completedByIdMod;

    @Column(name = "completed_on_mod")
    private Boolean completedOnMod;

    @Column(name = "status_mod")
    private Boolean statusMod;

    @Column(name = "signature_id_mod")
    private Boolean signatureIdMod;

    @Column(name = "archived_mod")
    private Boolean archivedMod;

    @Column(name = "parent_request_id_mod")
    private Boolean parentRequestIdMod;

    @Column(name = "feedback_mod")
    private Boolean feedbackMod;

    @Column(name = "parent_preventive_maintenance_id_mod")
    private Boolean parentPreventiveMaintenanceIdMod;


    @Column(name = "asset_id_mod")
    private Boolean assetIdMod;
}
