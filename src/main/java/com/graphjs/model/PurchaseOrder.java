package com.graphjs.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

//    private PurchaseOrderOrderCategory purchaseOrderOrderCategory;

    private Date dueDate;

    private String additionalDetail;

//    private Vendor vendor;

//    private Company requesterInformation;

//    private ShippingInformation shippingInformation;

//    private AdditionalInformation additionalInformation;

}
