package com.grash.mapper;

import com.grash.dto.TaskBasePatchDTO;
import com.grash.model.TaskBase;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskBaseMapper {
    TaskBase updateTaskBase(@MappingTarget TaskBase entity, TaskBasePatchDTO dto);
}
