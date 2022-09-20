package com.grash.mapper;

import com.grash.dto.UserResponseDTO;
import com.grash.dto.UserSignupRequest;
import com.grash.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User updateUser(@MappingTarget User entity, UserSignupRequest dto);


    @Mappings({@Mapping(source = "company.id", target = "companyId"),
            @Mapping(source = "company.companySettings.id", target = "companySettingsId"),
            @Mapping(source = "userSettings.id", target = "userSettingsId")})
    UserResponseDTO toDto(User model);

    @Mappings({})
    User toModel(UserSignupRequest dto);
}
