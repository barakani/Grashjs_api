package com.grash.mapper;

import com.grash.dto.MeterPatchDTO;
import com.grash.dto.MeterShowDTO;
import com.grash.model.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, AssetMapper.class, UserMapper.class})
public interface MeterMapper {
    Meter updateMeter(@MappingTarget Meter entity, MeterPatchDTO dto);

    @Mappings({})
    MeterPatchDTO toDto(Meter model);

    MeterShowDTO toShowDto(Meter model);

}
