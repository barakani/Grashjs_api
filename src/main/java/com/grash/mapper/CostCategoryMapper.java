package com.grash.mapper;

import com.grash.dto.CategoryPatchDTO;
import com.grash.model.CostCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CostCategoryMapper {
    CostCategory updateCostCategory(@MappingTarget CostCategory entity, CategoryPatchDTO dto);
}
