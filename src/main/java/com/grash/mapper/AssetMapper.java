package com.grash.mapper;

import com.grash.dto.AssetPatchDTO;
import com.grash.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    Asset updateAsset(@MappingTarget Asset entity, AssetPatchDTO dto);
}
