package com.grash.mapper;

import com.grash.dto.MeterPatchDTO;
import com.grash.model.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MeterMapper {
    Meter updateMeter(@MappingTarget Meter entity, MeterPatchDTO dto);
}
