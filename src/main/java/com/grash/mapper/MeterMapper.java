package com.grash.mapper;

import com.grash.dto.MeterPatchDTO;
import com.grash.model.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MeterMapper {
    Meter updateMeter(@MappingTarget Meter entity, MeterPatchDTO dto);

    @Mappings({})
    MeterPatchDTO toDto(Meter model);
}
