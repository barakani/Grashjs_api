package com.grash.mapper;

import com.grash.dto.GeneralPreferencesPatchDTO;
import com.grash.model.GeneralPreferences;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GeneralPreferencesMapper {
    GeneralPreferences updateGeneralPreferences(@MappingTarget GeneralPreferences entity, GeneralPreferencesPatchDTO dto);
}
