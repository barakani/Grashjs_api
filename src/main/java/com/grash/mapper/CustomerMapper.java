package com.grash.mapper;

import com.grash.dto.CustomerPatchDTO;
import com.grash.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer updateCustomer(@MappingTarget Customer entity, CustomerPatchDTO dto);
}
