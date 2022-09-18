package com.grash.mapper;

import com.grash.dto.MultiPartsPatchDTO;
import com.grash.model.MultiParts;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MultiPartsMapper {
    MultiParts updateMultiParts(@MappingTarget MultiParts entity, MultiPartsPatchDTO dto);

    @Mappings({})
    MultiPartsPatchDTO toDto(MultiParts model);
}
