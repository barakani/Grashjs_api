package com.graphjs.model;
import com.graphjs.model.abstracts.CategoryAbstract;
import com.graphjs.model.abstracts.FileAbstract;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class PurchaseOrderCategory extends CategoryAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
