package com.grash.mapper;

import com.grash.dto.CustomFieldPatchDTO;
import com.grash.model.CustomField;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomFieldMapper {
    CustomField updateCustomField(@MappingTarget CustomField entity, CustomFieldPatchDTO dto);
}
