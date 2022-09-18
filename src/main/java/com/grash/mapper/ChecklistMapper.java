package com.grash.mapper;

import com.grash.dto.ChecklistPatchDTO;
import com.grash.model.Checklist;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ChecklistMapper {
    Checklist updateChecklist(@MappingTarget Checklist entity, ChecklistPatchDTO dto);
}
