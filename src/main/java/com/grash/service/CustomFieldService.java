package com.grash.service;

import com.grash.dto.CustomFieldPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.CustomField;
import com.grash.model.User;
import com.grash.model.Vendor;
import com.grash.model.enums.RoleType;
import com.grash.repository.CustomFieldRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomFieldService {
    private final CustomFieldRepository customFieldRepository;
    private final VendorService vendorService;
    private final ModelMapper modelMapper;

    public CustomField create(CustomField CustomField) {
        return customFieldRepository.save(CustomField);
    }

    public CustomField update(Long id, CustomFieldPatchDTO customField) {
        if (customFieldRepository.existsById(id)) {
            CustomField savedCustomField = customFieldRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(customField, savedCustomField);
            return customFieldRepository.save(savedCustomField);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<CustomField> getAll() {
        return customFieldRepository.findAll();
    }

    public void delete(Long id) {
        customFieldRepository.deleteById(id);
    }

    public Optional<CustomField> findById(Long id) {
        return customFieldRepository.findById(id);
    }

    public boolean hasAccess(User user, CustomField customField) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(customField.getVendor().getCompany().getId());
    }

    public boolean canCreate(User user, CustomField customFieldReq) {
        Long companyId = user.getCompany().getId();

        Optional<Vendor> optionalVendor = vendorService.findById(customFieldReq.getVendor().getId());

        //@NotNull fields
        boolean first = optionalVendor.isPresent() && optionalVendor.get().getCompany().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(customFieldReq, CustomFieldPatchDTO.class));
    }

    public boolean canPatch(User user, CustomFieldPatchDTO customFieldReq) {
        return true;
    }
}
