package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    @NotNull
    private String name;

    @OneToOne
    private PurchaseOrderCategory purchaseOrderOrderCategory;

    private Date shippingDueDate;

    private String shippingAdditionalDetail;

    private String shippingShipToName;

    private String shippingCompanyName;

    private String shippingAddress;

    private String shippingCity;

    private String shippingState;

    private String shippingZipCode;

    private String shippingPhone;

    private String shippingFax;

    private Date additionalInfoDate;

    private String additionalInfoRequistionerName;

    private String additionalInfoShippingOrderCategory;

    private String additionalInfoTerm;

    private String additionalInfoNotes;

    @OneToOne
    private Vendor vendor;

    @OneToOne
    private Company requesterInformation;

}
