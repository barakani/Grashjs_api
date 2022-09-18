package com.grash.mapper;

import com.grash.dto.PurchaseOrderCategoryPatchDTO;
import com.grash.model.PurchaseOrderCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PurchaseOrderCategoryMapper {
    PurchaseOrderCategory updatePurchaseOrderCategory(@MappingTarget PurchaseOrderCategory entity, PurchaseOrderCategoryPatchDTO dto);
}
