package com.grash.mapper;

import com.grash.dto.TeamMiniDTO;
import com.grash.dto.TeamPatchDTO;
import com.grash.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team updateTeam(@MappingTarget Team entity, TeamPatchDTO dto);

    @Mappings({})
    TeamPatchDTO toDto(Team model);

    TeamMiniDTO toShowDto(Team model);
}
