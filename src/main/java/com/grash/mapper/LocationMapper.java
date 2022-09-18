package com.grash.mapper;

import com.grash.dto.LocationPatchDTO;
import com.grash.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location updateLocation(@MappingTarget Location entity, LocationPatchDTO dto);

    @Mappings({})
    LocationPatchDTO toDto(Location model);
}
