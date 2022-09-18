package com.grash.mapper;

import com.grash.dto.UserResponseDTO;
import com.grash.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({@Mapping(source = "company.id", target = "companyId")})
    UserResponseDTO toDto(User model);
}
