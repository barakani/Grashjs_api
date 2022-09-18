package com.grash.mapper;

import com.grash.dto.RequestPatchDTO;
import com.grash.model.Request;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    Request updateRequest(@MappingTarget Request entity, RequestPatchDTO dto);
}
