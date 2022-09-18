package com.grash.mapper;

import com.grash.dto.PurchaseOrderPatchDTO;
import com.grash.model.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    PurchaseOrder updatePurchaseOrder(@MappingTarget PurchaseOrder entity, PurchaseOrderPatchDTO dto);
}
