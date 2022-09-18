package com.grash.mapper;

import com.grash.dto.VendorPatchDTO;
import com.grash.model.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    Vendor updateVendor(@MappingTarget Vendor entity, VendorPatchDTO dto);
}
