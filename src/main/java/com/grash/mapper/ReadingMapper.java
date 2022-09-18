package com.grash.mapper;

import com.grash.dto.ReadingPatchDTO;
import com.grash.model.Reading;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReadingMapper {
    Reading updateReading(@MappingTarget Reading entity, ReadingPatchDTO dto);
}
