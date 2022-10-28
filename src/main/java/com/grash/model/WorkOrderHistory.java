package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.abstracts.CompanyAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderHistory extends CompanyAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WorkOrder workOrder;
}
