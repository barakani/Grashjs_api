package com.grash.mapper;

import com.grash.dto.RelationPatchDTO;
import com.grash.model.Relation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RelationMapper {
    Relation updateRelation(@MappingTarget Relation entity, RelationPatchDTO dto);
}
