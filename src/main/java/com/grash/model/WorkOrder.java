package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.WorkOrderBase;
import com.grash.model.enums.PermissionEntity;
import com.grash.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder extends WorkOrderBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private OwnUser completedBy;

    private Date completedOn;

    private Status status = Status.OPEN;

    @OneToOne
    private File signature;

    private boolean archived;

    @ManyToOne
    @JsonIgnore
    private Request parentRequest;


    @ManyToOne
    private PreventiveMaintenance parentPreventiveMaintenance;

    public boolean isAssignedTo(OwnUser user) {
        Collection<OwnUser> users = new ArrayList<>();
        if (this.getPrimaryUser() != null) {
            users.add(this.getPrimaryUser());
        }
        if (this.getTeam() != null) {
            users.addAll(this.getTeam().getUsers());
        }
        if (this.getAssignedTo() != null) {
            users.addAll(this.getAssignedTo());
        }
        return users.stream().anyMatch(user1 -> user1.getId().equals(user.getId()));
    }

    public boolean canBeEditedBy(OwnUser user) {
        return user.getRole().getEditOtherPermissions().contains(PermissionEntity.WORK_ORDERS)
                || this.getCreatedBy().equals(user.getId()) || isAssignedTo(user);
    }
}
