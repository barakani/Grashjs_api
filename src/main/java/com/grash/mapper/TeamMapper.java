package com.grash.mapper;

import com.grash.dto.TeamPatchDTO;
import com.grash.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team updateTeam(@MappingTarget Team entity, TeamPatchDTO dto);
}
