package com.grash.mapper;

import com.grash.dto.ImagePatchDTO;
import com.grash.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    Image updateImage(@MappingTarget Image entity, ImagePatchDTO dto);

    @Mappings({})
    ImagePatchDTO toDto(Image model);
}
