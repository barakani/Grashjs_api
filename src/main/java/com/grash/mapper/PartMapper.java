package com.grash.mapper;

import com.grash.dto.PartPatchDTO;
import com.grash.dto.PartShowDTO;
import com.grash.model.Part;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PartMapper {
    Part updatePart(@MappingTarget Part entity, PartPatchDTO dto);

    @Mappings({})
    PartPatchDTO toDto(Part model);

    @Mappings({})
    PartShowDTO toShowDto(Part model);
}
