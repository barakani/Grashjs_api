package com.grash.mapper;

import com.grash.dto.CategoryPatchDTO;
import com.grash.model.MeterCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MeterCategoryMapper {
    MeterCategory updateMeterCategory(@MappingTarget MeterCategory entity, CategoryPatchDTO dto);
}
