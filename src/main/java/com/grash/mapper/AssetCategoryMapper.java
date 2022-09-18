package com.grash.mapper;

import com.grash.dto.CategoryPatchDTO;
import com.grash.model.AssetCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssetCategoryMapper {
    AssetCategory updateAssetCategory(@MappingTarget AssetCategory entity, CategoryPatchDTO dto);
}
