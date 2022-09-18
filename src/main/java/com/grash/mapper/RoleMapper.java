package com.grash.mapper;

import com.grash.dto.RolePatchDTO;
import com.grash.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role updateRole(@MappingTarget Role entity, RolePatchDTO dto);
}
