package com.grash.mapper;

import com.grash.dto.AdditionalTimePatchDTO;
import com.grash.model.AdditionalTime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AdditionalTimeMapper {
    AdditionalTime updateAdditionalTime(@MappingTarget AdditionalTime entity, AdditionalTimePatchDTO dto);

    @Mappings({})
    AdditionalTimePatchDTO toPatchDto(AdditionalTime model);
}
